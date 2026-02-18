package com.shop.phoneshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int discountRate; // 10 = 10%

    private LocalDateTime createdAt;

    public Coupon(String name, int discountRate) {
        this.name = name;
        this.discountRate = discountRate;
        this.createdAt = LocalDateTime.now();
    }
}
