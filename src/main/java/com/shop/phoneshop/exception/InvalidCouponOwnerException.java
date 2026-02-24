package com.shop.phoneshop.exception;

public class InvalidCouponOwnerException extends RuntimeException {
    public InvalidCouponOwnerException() {
        super("본인의 쿠폰만 사용할 수 있습니다.");
    }
}