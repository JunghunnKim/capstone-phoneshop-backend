package com.shop.phoneshop.service;

import com.shop.phoneshop.dto.KakaoPayRequest;
import com.shop.phoneshop.dto.KakaoPayRequest.*;
import com.shop.phoneshop.dto.KakaoPayResponse;
import com.shop.phoneshop.dto.KakaoPayResponse.*;
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

        // 주문 생성
        Order order = orderService.createOrder(request);
        User user = order.getUser();

        // 쿠폰적용
        if (request.getUserCouponId() != null) {

            UserCoupon userCoupon = userCouponRepository.findById(request.getUserCouponId())
                    .orElseThrow(() -> new RuntimeException("쿠폰이 존재하지 않습니다."));

            if (userCoupon.isUsed()) {
                throw new RuntimeException("이미 사용된 쿠폰입니다.");
            }

            if (!userCoupon.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("본인의 쿠폰만 사용할 수 있습니다.");
            }

            int discountRate = userCoupon.getCoupon().getDiscountRate();

            int originalPrice = order.getFinalPrice();
            int discountAmount = originalPrice * discountRate / 100;
            int discountedPrice = originalPrice - discountAmount;

            order.updateFinalPrice(discountedPrice);
        }

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new RuntimeException("주문 상품이 존재하지 않습니다.");
        }

        // Payment 생성
        Payment payment = Payment.builder()
                .partnerOrderId(order.getId().toString())
                .partnerUserId(user.getId().toString())
                .amount(order.getFinalPrice())
                .status(PaymentStatus.READY)
                .createdAt(LocalDateTime.now())
                .build();

        // 쿠폰 선택했으면 Payment에 저장
        if (request.getUserCouponId() != null) {
            payment.applyCoupon(request.getUserCouponId());
        }

        paymentRepository.save(payment);

        // ---------------- 카카오 ready 호출 ----------------

        Map<String, String> parameters = new HashMap<>();

        parameters.put("cid", cid);
        parameters.put("partner_order_id", order.getId().toString());
        parameters.put("partner_user_id", user.getId().toString());

        // 다중상품 기준으로만 처리
        String firstItemName = order.getItems().get(0).getPhone().getName();
        int itemCount = order.getItems().size();

        String itemName = (itemCount > 1)
                ? firstItemName + " 외 " + (itemCount - 1) + "건"
                : firstItemName;

        int totalQuantity = order.getItems()
                .stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();

        parameters.put("item_name", itemName);
        parameters.put("quantity", String.valueOf(totalQuantity));
        parameters.put("total_amount", String.valueOf(order.getFinalPrice()));
        parameters.put("tax_free_amount", "0");
        parameters.put("approval_url", "http://localhost:8080/api/v1/kakao-pay/approve");
        parameters.put("cancel_url", "http://localhost:8080/api/v1/kakao-pay/cancel");
        parameters.put("fail_url", "http://localhost:8080/api/v1/kakao-pay/fail");

        HttpEntity<Map<String, String>> entity =
                new HttpEntity<>(parameters, getHeaders());

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<ReadyResponse> response =
                restTemplate.postForEntity(
                        "https://open-api.kakaopay.com/online/v1/payment/ready",
                        entity,
                        ReadyResponse.class
                );

        ReadyResponse body = response.getBody();

        if (body == null) {
            throw new RuntimeException("카카오페이 ready 응답이 없습니다.");
        }

        // tid 저장
        payment.updateTid(body.getTid());
        paymentRepository.save(payment);

        return body;
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
                .orElseThrow(() -> new RuntimeException("결제 대기 정보가 없습니다."));

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
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        order.markPaid();
        orderRepository.save(order);

        // 쿠폰 사용 처리
        if (payment.getUserCouponId() != null) {

            UserCoupon userCoupon = userCouponRepository.findById(
                    payment.getUserCouponId()
            ).orElseThrow(() -> new RuntimeException("쿠폰 없음"));

            userCoupon.markUsed();
        }

        return body;
    }
}