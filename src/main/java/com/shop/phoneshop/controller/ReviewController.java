package com.shop.phoneshop.controller;

import com.shop.phoneshop.dto.PhoneRatingResponse;
import com.shop.phoneshop.dto.PhoneReviewResponse;
import com.shop.phoneshop.dto.ReviewCreateRequest;
import com.shop.phoneshop.security.JwtTokenProvider;
import com.shop.phoneshop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtTokenProvider jwtTokenProvider;

    /// 평점 + 리뷰 등록
    @PostMapping
    public void addReview(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ReviewCreateRequest request
    ) {
        String token = authorization.replace("Bearer ", "");
        Long userId = jwtTokenProvider.getUserId(token);

        reviewService.addReview(userId, request);
    }

    /// 평균 평점 조회
    @GetMapping("/phones/{phoneId}/average")
    public PhoneRatingResponse getAverageRating(
            @PathVariable Long phoneId
    ) {
        return reviewService.getAverageRating(phoneId);
    }

    /// 핸드폰 리뷰 목록 조회
    @GetMapping("/phones/{phoneId}")
    public List<PhoneReviewResponse> getPhoneReviews(
            @PathVariable Long phoneId
    ) {
        return reviewService.getReviewsByPhone(phoneId);
    }

}
