package com.shop.phoneshop.controller;

import com.shop.phoneshop.dto.CouponCreateRequest;
import com.shop.phoneshop.exception.AdminOnlyException;
import com.shop.phoneshop.model.Role;
import com.shop.phoneshop.security.JwtTokenProvider;
import com.shop.phoneshop.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/totalSales")
    public Long getTotalSales(
            @RequestHeader("Authorization") String authorization
    ) {
        validateAdmin(authorization);
        return adminService.getTotalSales();
    }

    @PostMapping("/coupons")
    public String createCoupon(
            @RequestHeader("Authorization") String authorization,
            @RequestBody CouponCreateRequest request
    ) {
        validateAdmin(authorization);

        adminService.issueCouponToAllUsers(
                request.getName(),
                request.getDiscountRate()
        );

        return "쿠폰이 모든 회원에게 발급되었습니다.";
    }

    private void validateAdmin(String authorization) {
        Role role = extractRole(authorization);
        if (role != Role.ADMIN) {
            throw new AdminOnlyException();
        }
    }

    private Role extractRole(String authorization) {
        String token = authorization.replace("Bearer ", "");
        return jwtTokenProvider.getRole(token);
    }
}