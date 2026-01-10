package com.shop.phoneshop.controller;

import com.shop.phoneshop.dto.FavoriteRequest;
import com.shop.phoneshop.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    // 관심 기종 등록
    @PostMapping
    public void addFavorite(@RequestBody FavoriteRequest request) {
        favoriteService.addFavorite(request);
    }
}
