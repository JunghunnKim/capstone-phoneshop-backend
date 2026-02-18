package com.shop.phoneshop.service;

import com.shop.phoneshop.dto.UserCouponResponse;
import com.shop.phoneshop.model.UserCoupon;
import com.shop.phoneshop.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final UserCouponRepository userCouponRepository;

    @Transactional(readOnly = true)
    public List<UserCouponResponse> getMyAvailableCoupons(Long userId) {

        List<UserCoupon> userCoupons =
                userCouponRepository.findByUserIdAndUsedFalse(userId);

        return userCoupons.stream()
                .map(uc -> new UserCouponResponse(
                        uc.getId(),
                        uc.getCoupon().getName(),
                        uc.getCoupon().getDiscountRate()
                ))
                .toList();
    }
}

