package com.shop.phoneshop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponCreateRequest {

    private String name;
    private int discountRate;
}

