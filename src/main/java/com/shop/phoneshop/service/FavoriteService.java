package com.shop.phoneshop.service;

import com.shop.phoneshop.dto.FavoritePhoneResponse;
import com.shop.phoneshop.model.Favorite;
import com.shop.phoneshop.model.Phone;
import com.shop.phoneshop.model.User;
import com.shop.phoneshop.repository.FavoriteRepository;
import com.shop.phoneshop.repository.PhoneRepository;
import com.shop.phoneshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;

    /// 관심 기종 등록
    public void addFavorite(Long userId, Long phoneId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Phone phone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new IllegalArgumentException("폰 없음"));

        boolean exists =
                favoriteRepository.existsByUserAndPhone(user, phone);

        if (exists) {
            throw new IllegalStateException("이미 관심 등록됨");
        }

        favoriteRepository.save(new Favorite(user, phone));
    }

    /// 관심 기종 조회
    @Transactional(readOnly = true)
    public List<FavoritePhoneResponse> getMyFavorites(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        return favoriteRepository.findByUser(user).stream()
                .map(FavoritePhoneResponse::from)
                .toList();
    }

    /// 관심 기종 삭제
    public void deleteFavorite(Long userId, Long phoneId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Phone phone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new IllegalArgumentException("폰 없음"));

        Favorite favorite = favoriteRepository
                .findByUserAndPhone(user, phone)
                .orElseThrow(() -> new IllegalArgumentException("관심 등록 안됨"));

        favoriteRepository.delete(favorite);
    }
}
