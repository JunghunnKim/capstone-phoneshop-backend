package com.shop.phoneshop.model;

public enum OrderStatus {
    CREATED,    // 주문 생성 (결제 전)
    PAID,       // 결제 완료
    CANCELLED   // 주문 취소
}
