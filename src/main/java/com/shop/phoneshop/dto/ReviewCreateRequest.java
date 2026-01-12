package com.shop.phoneshop.dto;

import lombok.Getter;

@Getter
public class ReviewCreateRequest {

    private Long phoneId;
    private int rating;
    private String content;
}
