package com.shop.phoneshop.controller;

import com.shop.phoneshop.dto.FavoriteDeleteRequest;
import com.shop.phoneshop.dto.FavoritePhoneResponse;
import com.shop.phoneshop.dto.FavoriteRequest;
import com.shop.phoneshop.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    /// 관심 기종 등록
    @PostMapping
    public void addFavorite(@RequestBody FavoriteRequest request) {
        favoriteService.addFavorite(request);
    }

    /// 관심 기종 조회
    @GetMapping("/users/{userId}")
    public List<FavoritePhoneResponse> getUserFavorites(@PathVariable Long userId) {
        return favoriteService.getFavoritesByUser(userId);
    }

    /// 관심 기종 삭제
    @DeleteMapping
    public void removeFavorite(@RequestBody FavoriteDeleteRequest request) {
        favoriteService.removeFavorite(request);
    }

}
