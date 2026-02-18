package com.shop.phoneshop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PhoneCreateRequest {

    private String name;
    private String brand;
    private int price;

    //스펙
    private String display;
    private String processor;
    private String ram;
    private String storage;
    private String battery;
    private String camera;
}
