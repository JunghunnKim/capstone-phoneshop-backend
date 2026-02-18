package com.shop.phoneshop.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Coupon coupon;

    private boolean used = false;

    public UserCoupon(User user, Coupon coupon) {
        this.user = user;
        this.coupon = coupon;
    }

    public void markUsed() {
        this.used = true;
    }
}

