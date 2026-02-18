package com.shop.phoneshop.controller;

import com.shop.phoneshop.security.JwtTokenProvider;
import com.shop.phoneshop.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/coupons")
@RequiredArgsConstructor
public class UserCouponController {

    private final CouponService couponService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public ResponseEntity<?> getMyCoupons(
            @RequestHeader("Authorization") String authorization
    ) {

        String token = authorization.replace("Bearer ", "");
        Long userId = jwtTokenProvider.getUserId(token);

        return ResponseEntity.ok(
                couponService.getMyAvailableCoupons(userId)
        );
    }
}
