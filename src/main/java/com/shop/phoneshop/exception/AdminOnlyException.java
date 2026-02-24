package com.shop.phoneshop.exception;

import org.springframework.http.HttpStatus;

public class AdminOnlyException extends RuntimeException {

    private final HttpStatus status = HttpStatus.FORBIDDEN;

    public AdminOnlyException() {
        super("관리자만 접근 가능합니다.");
    }

    public HttpStatus getStatus() {
        return status;
    }
}