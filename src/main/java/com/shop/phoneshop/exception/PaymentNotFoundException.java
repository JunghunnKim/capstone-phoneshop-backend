package com.shop.phoneshop.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException() {
        super("결제 대기 정보가 없습니다.");
    }
}