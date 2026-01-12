package com.shop.phoneshop.controller;

import com.shop.phoneshop.dto.ReviewCreateRequest;
import com.shop.phoneshop.security.JwtTokenProvider;
import com.shop.phoneshop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
