package com.shop.phoneshop.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PhoneRatingResponse {

    private Long phoneId;
    private double averageRating;
}
