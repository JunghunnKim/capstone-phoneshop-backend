package com.shop.phoneshop.exception;

public class CouponNotFoundException extends RuntimeException {
    public CouponNotFoundException() {
        super("쿠폰이 존재하지 않습니다.");
    }
}