package com.shop.phoneshop.controller;

import com.shop.phoneshop.model.Role;
import com.shop.phoneshop.service.AdminService;
import com.shop.phoneshop.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/totalSales")
    public ResponseEntity<?> getTotalSales(
            @RequestHeader("Authorization") String authorization
    ) {

        Role role = extractRole(authorization);

        if (role != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("관리자만 접근 가능합니다.");
        }

        Long totalSales = adminService.getTotalSales();

        return ResponseEntity.ok(totalSales);
    }

    private Role extractRole(String authorization) {
        String token = authorization.replace("Bearer ", "");
        return jwtTokenProvider.getRole(token);
    }
}
