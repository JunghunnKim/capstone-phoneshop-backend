package com.shop.phoneshop.dto;

import com.shop.phoneshop.model.CartItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemResponse {

    private Long phoneId;
    private String name;
    private String brand;
    private int price;
    private String imageUrl;
    private int quantity;

    public static CartItemResponse from(CartItem cartItem, String baseUrl) {
        return CartItemResponse.builder()
                .phoneId(cartItem.getPhone().getId())
                .name(cartItem.getPhone().getName())
                .brand(cartItem.getPhone().getBrand())
                .price(cartItem.getPhone().getPrice())
                .imageUrl(baseUrl + cartItem.getPhone().getImageUrl())
                .quantity(cartItem.getQuantity())
                .build();
    }

}
