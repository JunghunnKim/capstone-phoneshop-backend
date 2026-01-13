package com.shop.phoneshop.dto;

import lombok.Getter;

@Getter
public class ReviewCreateRequest {

    private Long phoneId;
    private double rating;
    private String content;
}
