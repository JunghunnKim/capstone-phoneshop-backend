package com.shop.phoneshop.controller;

import com.shop.phoneshop.dto.KakaoPayRequest.*;
import com.shop.phoneshop.dto.KakaoPayResponse.*;
import com.shop.phoneshop.service.KakaoPayProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/kakao-pay")
public class KakaoPayController {

    private final KakaoPayProvider kakaoPayProvider;

    // 카카오페이 결제 준비
    @PostMapping("/ready")
    public ReadyResponse ready(@RequestBody OrderRequest request) {
        return kakaoPayProvider.ready(request);
    }

    // 카카오페이 결제 승인
    @GetMapping("/approve")
    public RedirectView approve(@RequestParam("pg_token") String pgToken) {
        ApproveResponse response = kakaoPayProvider.approve(pgToken);
        String orderId = response.getPartner_order_id();
        return new RedirectView("http://localhost:3000/order-complete/" + orderId);
    }

    // 결제 취소
    @GetMapping("/cancel")
    public RedirectView cancel() {
        return new RedirectView("http://localhost:3000/cart?status=cancelled");
    }

    // 결제 실패
    @GetMapping("/fail")
    public RedirectView fail() {
        return new RedirectView("http://localhost:3000/cart?status=failed");
    }

}