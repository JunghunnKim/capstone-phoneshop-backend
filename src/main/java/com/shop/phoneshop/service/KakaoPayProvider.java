package com.shop.phoneshop.service;

import com.shop.phoneshop.dto.KakaoPayRequest;
import com.shop.phoneshop.dto.KakaoPayRequest.*;
import com.shop.phoneshop.dto.KakaoPayResponse;
import com.shop.phoneshop.dto.KakaoPayResponse.*;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class KakaoPayProvider {

    @Value("${kakaopay.secretKey}")
    private String secretKey;

    @Value("${kakaopay.cid}")
    private String cid;

    public ReadyResponse ready(OrderRequest request) {

        Map<String, String> parameters = new HashMap<>();

        parameters.put("cid", cid); // 가맹점 코드, 테스트용은 TC0ONETIME
        parameters.put("partner_order_id", "1234567890"); // 주문번호, 임시 : 1234567890
        parameters.put("partner_user_id", "1234567890"); // 회원아이디, 임시 : 1234567890
        parameters.put("item_name", request.getItemName()); // 상품명
        parameters.put("quantity", request.getQuantity()); // 상품 수량
        parameters.put("total_amount", request.getTotalPrice()); // 상품 총액
        parameters.put("tax_free_amount", "0"); // 상품 비과세 금액
        parameters.put("approval_url", "https://localhost:8080/api/v1/kakao-pay/approve"); // 결제 성공 시 redirct URL
        parameters.put("cancel_url", "https://localhost:8080/api/v1/kakao-pay/cancel"); // 결제 취소 시
        parameters.put("fail_url", "https://localhost:8080/kakao-pay/fail"); // 결제 실패 시

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(parameters, getHeaders());

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";
        ResponseEntity<ReadyResponse> response = restTemplate.postForEntity(url, entity, ReadyResponse.class);

        SessionProvider.addAttribute("tid",
                Objects.requireNonNull(response.getBody()).getTid());

        return response.getBody();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + secretKey);
        headers.add("Content-type", "application/json");
        return headers;
    }



    public ApproveResponse approve(String pgToken) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("tid", SessionProvider.getStringAttribute("tid"));
        parameters.put("partner_order_id", "1234567890");
        parameters.put("partner_user_id", "1234567890");
        parameters.put("pg_token", pgToken); // 결제승인 요청을 인증하는 토큰

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(parameters, getHeaders());

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/approve";
        ResponseEntity<ApproveResponse> response = restTemplate.postForEntity(url, entity, ApproveResponse.class);

        return response.getBody();
    }



    public static class SessionProvider {

        public static void addAttribute(String key, Object value) {
            Objects.requireNonNull(RequestContextHolder.getRequestAttributes())
                    .setAttribute(key, value, RequestAttributes.SCOPE_SESSION);
        }

        public static Object getAttribute(String key) {
            return Objects.requireNonNull(RequestContextHolder.getRequestAttributes())
                    .getAttribute(key, RequestAttributes.SCOPE_SESSION);
        }

        public static String getStringAttribute(String key) {
            return (String) getAttribute(key);
        }
    }
}