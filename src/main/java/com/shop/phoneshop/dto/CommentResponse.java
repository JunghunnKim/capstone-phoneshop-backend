package com.shop.phoneshop.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponse {

    private Long commentId;
    private Long userId;
    private String userName;
    private String content;
}
