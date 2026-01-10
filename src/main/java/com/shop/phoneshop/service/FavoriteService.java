package com.shop.phoneshop.service;

import com.shop.phoneshop.dto.FavoriteDeleteRequest;
import com.shop.phoneshop.dto.FavoritePhoneResponse;
import com.shop.phoneshop.dto.FavoriteRequest;
import com.shop.phoneshop.model.Favorite;
import com.shop.phoneshop.model.Phone;
import com.shop.phoneshop.model.User;
import com.shop.phoneshop.repository.FavoriteRepository;
import com.shop.phoneshop.repository.PhoneRepository;
import com.shop.phoneshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;

    /// 관심 기종 등록
    public void addFavorite(FavoriteRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        Phone phone = phoneRepository.findById(request.getPhoneId())
                .orElseThrow(() -> new IllegalArgumentException("핸드폰이 존재하지 않습니다."));

        // 중복 찜 방지
        if (favoriteRepository.findByUserIdAndPhoneId(user.getId(), phone.getId()).isPresent()) {
            throw new IllegalStateException("이미 관심 기종으로 등록되어 있습니다.");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .phone(phone)
                .build();

        favoriteRepository.save(favorite);
    }

    /// 관심 기종 조회
    public List<FavoritePhoneResponse> getFavoritesByUser(Long userId) {

        return favoriteRepository.findAllByUserId(userId)
                .stream()
                .map(favorite -> {
                    Phone phone = favorite.getPhone();

                    return FavoritePhoneResponse.builder()
                            .phoneId(phone.getId())
                            .name(phone.getName())
                            .brand(phone.getBrand())
                            .price(phone.getPrice())
                            .imageUrl(phone.getImageUrl())
                            .build();
                })
                .toList();
    }

    /// 관심 기종 삭제
    public void removeFavorite(FavoriteDeleteRequest request) {

        Favorite favorite = favoriteRepository
                .findByUserIdAndPhoneId(request.getUserId(), request.getPhoneId())
                .orElseThrow(() -> new IllegalArgumentException("관심 기종이 존재하지 않습니다."));

        favoriteRepository.delete(favorite);
    }
}
