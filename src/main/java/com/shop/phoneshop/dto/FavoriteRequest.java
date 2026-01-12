package com.shop.phoneshop.dto;

import lombok.Getter;

@Getter
public class FavoriteRequest {
    private Long userId;
    private Long phoneId;
}
