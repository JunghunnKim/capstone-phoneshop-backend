package com.shop.phoneshop.service;

import com.shop.phoneshop.model.Coupon;
import com.shop.phoneshop.model.User;
import com.shop.phoneshop.model.UserCoupon;
import com.shop.phoneshop.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;


    public Long getTotalSales() {
        return orderRepository.getTotalSales();
    }


    @Transactional
    public void issueCouponToAllUsers(String name, int discountRate) {

        // 쿠폰 생성
        Coupon coupon = new Coupon(name, discountRate);
        couponRepository.save(coupon);

        // 모든 회원 조회
        List<User> users = userRepository.findAll();

        // 회원별 쿠폰 생성
        for (User user : users) {
            UserCoupon userCoupon = new UserCoupon(user, coupon);
            userCouponRepository.save(userCoupon);
        }
    }


}

