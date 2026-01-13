package com.shop.phoneshop.controller;

import com.shop.phoneshop.dto.CartItemAddRequest;
import com.shop.phoneshop.dto.CartItemDecreaseRequest;
import com.shop.phoneshop.dto.CartItemResponse;
import com.shop.phoneshop.dto.CartResponse;
import com.shop.phoneshop.security.JwtTokenProvider;
import com.shop.phoneshop.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final JwtTokenProvider jwtTokenProvider;

    /// 장바구니 상품 추가 (+1)
    @PostMapping("/items")
    public void addItem(
            @RequestHeader("Authorization") String authorization,
            @RequestBody CartItemAddRequest request
    ) {
        Long userId = extractUserId(authorization);
        cartService.addItem(userId, request);
    }

    /// 장바구니 수량 감소 (-1)
    @PostMapping("/items/decrease")
    public void decreaseItem(
            @RequestHeader("Authorization") String authorization,
            @RequestBody CartItemDecreaseRequest request
    ) {
        Long userId = extractUserId(authorization);
        cartService.decreaseItem(userId, request);
    }

    /// 장바구니 조회
    @GetMapping
    public CartResponse getMyCart(
            @RequestHeader("Authorization") String authorization
    ) {
        Long userId = extractUserId(authorization);
        return cartService.getMyCart(userId);
    }

    /// 장바구니 품목 전체 삭제
    @DeleteMapping
    public void clearCart(
            @RequestHeader("Authorization") String authorization
    ) {
        Long userId = extractUserId(authorization);
        cartService.clearCart(userId);
    }

    private Long extractUserId(String authorization) {
        String token = authorization.replace("Bearer ", "");
        return jwtTokenProvider.getUserId(token);
    }

}
