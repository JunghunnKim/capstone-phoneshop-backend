package com.shop.phoneshop.exception;

public class PhoneNotFoundException extends RuntimeException {
    public PhoneNotFoundException() {
        super("상품을 찾을 수 없습니다.");
    }
}