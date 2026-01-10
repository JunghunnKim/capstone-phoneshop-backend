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
}
