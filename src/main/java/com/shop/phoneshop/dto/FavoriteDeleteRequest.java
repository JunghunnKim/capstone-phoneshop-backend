package com.shop.phoneshop.dto;

import lombok.Getter;

@Getter
public class FavoriteDeleteRequest {
    private Long userId;
    private Long phoneId;
}
