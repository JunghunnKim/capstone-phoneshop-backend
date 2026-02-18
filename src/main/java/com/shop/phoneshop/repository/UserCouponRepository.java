package com.shop.phoneshop.repository;

import com.shop.phoneshop.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    List<UserCoupon> findByUserIdAndUsedFalse(Long userId);
}

