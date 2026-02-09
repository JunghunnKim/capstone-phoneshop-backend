package com.shop.phoneshop.controller;

import com.shop.phoneshop.dto.KakaoPayRequest;
import com.shop.phoneshop.dto.KakaoPayRequest.*;
import com.shop.phoneshop.dto.KakaoPayResponse;
import com.shop.phoneshop.dto.KakaoPayResponse.*;
import com.shop.phoneshop.service.KakaoPayProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public ApproveResponse approve(@RequestParam("pg_token") String pgToken) {
        return kakaoPayProvider.approve(pgToken);
    }

}