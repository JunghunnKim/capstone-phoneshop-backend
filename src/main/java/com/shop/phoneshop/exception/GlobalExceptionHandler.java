package com.shop.phoneshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 관리자 권한 없음
    @ExceptionHandler(AdminOnlyException.class)
    public ResponseEntity<?> handleAdminOnlyException(AdminOnlyException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(e.getMessage());
    }

    // ❌ 리소스 없음 (404)
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(NoSuchElementException e) {
        return Map.of("message", e.getMessage());
    }

    // ❌ 잘못된 요청 (로그인 실패, 검증 실패 등)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(IllegalArgumentException e) {
        return Map.of("message", e.getMessage());
    }
}
