package com.shop.phoneshop.service;

import com.shop.phoneshop.dto.KakaoPayRequest;
import com.shop.phoneshop.dto.KakaoPayRequest.*;
import com.shop.phoneshop.dto.KakaoPayResponse;
import com.shop.phoneshop.dto.KakaoPayResponse.*;
import com.shop.phoneshop.exception.*;
import com.shop.phoneshop.model.*;
import com.shop.phoneshop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class KakaoPayProvider {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final UserCouponRepository userCouponRepository;


    @Value("${kakaopay.secretKey}")
    private String secretKey;

    @Value("${kakaopay.cid}")
    private String cid;

    public ReadyResponse ready(OrderRequest request) {

        Order order = orderService.createOrder(request);
        User user = order.getUser();

        // 쿠폰 적용
        if (request.getUserCouponId() != null) {

            UserCoupon userCoupon = userCouponRepository.findById(request.getUserCouponId())
                    .orElseThrow(CouponNotFoundException::new);

            if (userCoupon.isUsed()) {
                throw new CouponAlreadyUsedException();
            }

            if (!userCoupon.getUser().getId().equals(user.getId())) {
                throw new InvalidCouponOwnerException();
            }

            int discountRate = userCoupon.getCoupon().getDiscountRate();
            int originalPrice = order.getFinalPrice();
            int discountedPrice = originalPrice - (originalPrice * discountRate / 100);

            order.updateFinalPrice(discountedPrice);
        }

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new EmptyOrderItemException();
        }

        Payment payment = Payment.builder()
                .partnerOrderId(order.getId().toString())
                .partnerUserId(user.getId().toString())
                .amount(order.getFinalPrice())
                .status(PaymentStatus.READY)
                .createdAt(LocalDateTime.now())
                .build();

        if (request.getUserCouponId() != null) {
            payment.applyCoupon(request.getUserCouponId());
        }

        paymentRepository.save(payment);

        ReadyResponse body = callKakaoReady(order, user);

        payment.updateTid(body.getTid());
        paymentRepository.save(payment);

        return body;
    }

    private ReadyResponse callKakaoReady(Order order, User user) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("partner_order_id", order.getId().toString());
        parameters.put("partner_user_id", user.getId().toString());

        String firstItemName = order.getItems().get(0).getPhone().getName();
        int itemCount = order.getItems().size();

        parameters.put("item_name",
                itemCount > 1 ? firstItemName + " 외 " + (itemCount - 1) + "건" : firstItemName);

        parameters.put("quantity", String.valueOf(
                order.getItems().stream().mapToInt(OrderItem::getQuantity).sum()
        ));
        parameters.put("total_amount", String.valueOf(order.getFinalPrice()));
        parameters.put("tax_free_amount", "0");
        parameters.put("approval_url", "http://localhost:8080/api/v1/kakao-pay/approve");
        parameters.put("cancel_url", "http://localhost:8080/api/v1/kakao-pay/cancel");
        parameters.put("fail_url", "http://localhost:8080/api/v1/kakao-pay/fail");

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<ReadyResponse> response =
                    restTemplate.postForEntity(
                            "https://open-api.kakaopay.com/online/v1/payment/ready",
                            new HttpEntity<>(parameters, getHeaders()),
                            ReadyResponse.class
                    );

            if (response.getBody() == null) {
                throw new KakaoPayApiException("카카오페이 ready 응답이 없습니다.");
            }

            return response.getBody();

        } catch (Exception e) {
            log.error("카카오페이 ready 실패", e);
            throw new KakaoPayApiException("카카오페이 결제 준비 중 오류 발생");
        }
    }


    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + secretKey);
        headers.add("Content-type", "application/json");
        return headers;
    }



    public ApproveResponse approve(String pgToken) {

        // tid를 세션이 아니라 DB에서 조회해야 함
        Payment payment = paymentRepository.findTopByStatusOrderByCreatedAtDesc(PaymentStatus.READY)
                .orElseThrow(PaymentNotFoundException::new);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("tid", payment.getTid());
        parameters.put("partner_order_id", payment.getPartnerOrderId());
        parameters.put("partner_user_id", payment.getPartnerUserId());
        parameters.put("pg_token", pgToken);

        HttpEntity<Map<String, String>> entity =
                new HttpEntity<>(parameters, getHeaders());

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<ApproveResponse> response =
                restTemplate.postForEntity(
                        "https://open-api.kakaopay.com/online/v1/payment/approve",
                        entity,
                        ApproveResponse.class
                );

        ApproveResponse body = response.getBody();

        // Payment 성공 처리
        payment.markSuccess();
        paymentRepository.save(payment);

        // Order 상태 변경
        Long orderId = Long.parseLong(payment.getPartnerOrderId());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.markPaid();
        orderRepository.save(order);

        // 쿠폰 사용 처리
        if (payment.getUserCouponId() != null) {

            UserCoupon userCoupon = userCouponRepository.findById(
                    payment.getUserCouponId()
            ).orElseThrow(CouponNotFoundException::new);

            userCoupon.markUsed();
        }

        return body;
    }
}