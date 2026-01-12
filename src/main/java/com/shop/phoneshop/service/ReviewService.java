package com.shop.phoneshop.service;

import com.shop.phoneshop.dto.ReviewCreateRequest;
import com.shop.phoneshop.model.Phone;
import com.shop.phoneshop.model.Review;
import com.shop.phoneshop.model.User;
import com.shop.phoneshop.repository.PhoneRepository;
import com.shop.phoneshop.repository.ReviewRepository;
import com.shop.phoneshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;

    /// 평점 + 리뷰 등록
    public void addReview(Long userId, ReviewCreateRequest request) {

        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("평점은 1~5 사이여야 합니다");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Phone phone = phoneRepository.findById(request.getPhoneId())
                .orElseThrow(() -> new IllegalArgumentException("핸드폰 없음"));

        boolean exists =
                reviewRepository.existsByUserAndPhone(user, phone);

        if (exists) {
            throw new IllegalStateException("이미 해당 핸드폰에 리뷰를 작성했습니다");
        }

        Review review = new Review(
                user,
                phone,
                request.getRating(),
                request.getContent()
        );

        reviewRepository.save(review);
    }
}
