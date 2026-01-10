package com.shop.phoneshop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PhoneCreateRequest {

    private String modelName;
    private String brand;
    private int price;
    private int stock;
}
