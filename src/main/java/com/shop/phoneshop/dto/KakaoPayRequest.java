package com.shop.phoneshop.dto;

import lombok.*;

import java.util.List;

public class KakaoPayRequest {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OrderRequest {

        private Long userId;
        private List<OrderItemRequest> items;
        private Long userCouponId;
    }

    @Getter
    public static class OrderItemRequest {

        private Long phoneId;
        private int quantity;

    }
}