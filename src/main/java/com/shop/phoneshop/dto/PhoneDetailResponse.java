package com.shop.phoneshop.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PhoneDetailResponse {

    private Long id;
    private String name;
    private String brand;
    private int price;
    private String imageUrl;

    // 스펙
    private String display;
    private String processor;
    private String ram;
    private String storage;
    private String battery;
    private String camera;
}
