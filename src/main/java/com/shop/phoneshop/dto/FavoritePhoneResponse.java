package com.shop.phoneshop.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FavoritePhoneResponse {

    private Long phoneId;
    private String name;
    private String brand;
    private int price;
    private String imageUrl;
}
