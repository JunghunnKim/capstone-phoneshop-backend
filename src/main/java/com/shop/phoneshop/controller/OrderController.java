package com.shop.phoneshop.controller;

import com.shop.phoneshop.dto.OrderHistoryResponse;
import com.shop.phoneshop.security.JwtTokenProvider;
import com.shop.phoneshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/history")
    public List<OrderHistoryResponse> getPurchaseHistory(
            @RequestHeader("Authorization") String authorization
    ) {
        Long userId = extractUserId(authorization);
        return orderService.getPurchaseHistory(userId);
    }

    private Long extractUserId(String authorization) {
        String token = authorization.replace("Bearer ", "");
        return jwtTokenProvider.getUserId(token);
    }
}
