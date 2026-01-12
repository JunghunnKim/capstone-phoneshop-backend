package com.shop.phoneshop.dto;

import com.shop.phoneshop.model.Favorite;
import com.shop.phoneshop.model.Phone;
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

    public static FavoritePhoneResponse from(Favorite favorite) {
        Phone phone = favorite.getPhone();

        return FavoritePhoneResponse.builder()
                .phoneId(phone.getId())
                .name(phone.getName())
                .brand(phone.getBrand())
                .price(phone.getPrice())
                .imageUrl(phone.getImageUrl())
                .build();
    }
}
