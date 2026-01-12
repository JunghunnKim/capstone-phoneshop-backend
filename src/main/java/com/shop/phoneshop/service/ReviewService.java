package com.shop.phoneshop.service;

import com.shop.phoneshop.dto.PhoneRatingResponse;
import com.shop.phoneshop.dto.PhoneReviewResponse;
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

import java.util.List;

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

    /// 평균 평점 조회
    @Transactional(readOnly = true)
    public PhoneRatingResponse getAverageRating(Long phoneId) {

        Double avg = reviewRepository.findAverageRatingByPhoneId(phoneId);

        return PhoneRatingResponse.builder()
                .phoneId(phoneId)
                .averageRating(avg == null ? 0.0 : Math.round(avg * 10) / 10.0)
                .build();
    }

    /// 핸드폰 리뷰 목록 조회
    @Transactional(readOnly = true)
    public List<PhoneReviewResponse> getReviewsByPhone(Long phoneId) {

        Phone phone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new IllegalArgumentException("폰 없음"));

        return reviewRepository.findByPhoneOrderByCreatedAtDesc(phone).stream()
                .map(PhoneReviewResponse::from)
                .toList();
    }

    /** 리뷰 삭제 */
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰 없음"));

        // 작성자 검증
        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalStateException("리뷰 삭제 권한 없음");
        }

        reviewRepository.delete(review);
    }
}
