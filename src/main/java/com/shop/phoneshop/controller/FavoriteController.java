package com.shop.phoneshop.controller;

import com.shop.phoneshop.dto.FavoritePhoneResponse;
import com.shop.phoneshop.security.JwtTokenProvider;
import com.shop.phoneshop.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final JwtTokenProvider jwtTokenProvider;

    /// 관심 기종 등록
    @PostMapping
    public void addFavorite(
            @RequestHeader("Authorization") String authorization,
            @RequestParam Long phoneId
    ) {
        Long userId = extractUserId(authorization);
        favoriteService.addFavorite(userId, phoneId);
    }

    /// 내 관심 기종 조회
    @GetMapping
    public List<FavoritePhoneResponse> getMyFavorites(
            @RequestHeader("Authorization") String authorization
    ) {
        Long userId = extractUserId(authorization);
        return favoriteService.getMyFavorites(userId);
    }

    /// 관심 기종 삭제
    @DeleteMapping("/{phoneId}")
    public void deleteFavorite(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long phoneId
    ) {
        Long userId = extractUserId(authorization);
        favoriteService.deleteFavorite(userId, phoneId);
    }

    private Long extractUserId(String authorization) {
        String token = authorization.replace("Bearer ", "");
        return jwtTokenProvider.getUserId(token);
    }
}
