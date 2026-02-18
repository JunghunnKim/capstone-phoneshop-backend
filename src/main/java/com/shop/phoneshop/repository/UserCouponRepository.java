package com.shop.phoneshop.repository;

import com.shop.phoneshop.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {}

