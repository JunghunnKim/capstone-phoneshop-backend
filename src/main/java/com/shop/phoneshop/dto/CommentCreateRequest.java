package com.shop.phoneshop.dto;

import lombok.Getter;

@Getter
public class CommentCreateRequest {

    private Long userId;
    private Long phoneId;
    private String content;
}
