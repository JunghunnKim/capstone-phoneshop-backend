package com.shop.phoneshop.dto;

import com.shop.phoneshop.model.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PhoneReviewResponse {

    private Long reviewId;
    private int rating;
    private String content;
    private Long userId;
    private String userName;
    private LocalDateTime createdAt;

    public static PhoneReviewResponse from(Review review) {
        return PhoneReviewResponse.builder()
                .reviewId(review.getId())
                .rating(review.getRating())
                .content(review.getContent())
                .userId(review.getUser().getId())
                .userName(review.getUser().getName())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
