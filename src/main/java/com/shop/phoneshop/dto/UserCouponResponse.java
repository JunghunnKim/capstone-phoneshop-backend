package com.shop.phoneshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCouponResponse {

    private Long userCouponId;
    private String couponName;
    private int discountRate;
}

