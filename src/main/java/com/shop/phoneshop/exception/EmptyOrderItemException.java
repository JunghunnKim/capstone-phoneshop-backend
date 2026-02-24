package com.shop.phoneshop.exception;

public class EmptyOrderItemException extends RuntimeException {
    public EmptyOrderItemException() {
        super("주문 상품이 존재하지 않습니다.");
    }
}