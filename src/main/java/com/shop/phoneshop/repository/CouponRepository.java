package com.shop.phoneshop.repository;

import com.shop.phoneshop.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {}

