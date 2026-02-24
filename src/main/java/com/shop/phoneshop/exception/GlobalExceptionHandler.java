package com.shop.phoneshop.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* =======================
       400 BAD REQUEST
       ======================= */

    @ExceptionHandler({
            IllegalArgumentException.class,
            EmptyOrderItemException.class,
            CouponAlreadyUsedException.class,
            InvalidCouponOwnerException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException e) {
        return build(HttpStatus.BAD_REQUEST, e);
    }

    /* =======================
       403 FORBIDDEN
       ======================= */

    @ExceptionHandler(AdminOnlyException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(AdminOnlyException e) {
        return build(HttpStatus.FORBIDDEN, e);
    }

    /* =======================
       404 NOT FOUND
       ======================= */

    @ExceptionHandler({
            UserNotFoundException.class,
            PhoneNotFoundException.class,
            OrderNotFoundException.class,
            CouponNotFoundException.class,
            PaymentNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException e) {
        return build(HttpStatus.NOT_FOUND, e);
    }

    /* =======================
       502 BAD GATEWAY
       ======================= */

    @ExceptionHandler(KakaoPayApiException.class)
    public ResponseEntity<ErrorResponse> handleKakaoPay(KakaoPayApiException e) {
        log.error("KakaoPay API Error", e);
        return build(HttpStatus.BAD_GATEWAY, e);
    }

    /* =======================
       500 INTERNAL SERVER ERROR
       ======================= */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandled(Exception e) {
        log.error("Unhandled Exception", e);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    /* =======================
       공통 응답 빌더
       ======================= */

    private ResponseEntity<ErrorResponse> build(HttpStatus status, Exception e) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        status.value(),
                        status.getReasonPhrase(),
                        e.getMessage()
                ));
    }
}