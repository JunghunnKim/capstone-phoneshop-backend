package com.shop.phoneshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelName;     // 모델명
    private String brand;         // 브랜드 (삼성, 애플 등)
    private int price;            // 가격
    private int stock;            // 재고 수량
    private String imageUrl;      // 이미지 경로

    public Phone(String modelName, String brand, int price, int stock, String imageUrl) {
        this.modelName = modelName;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }
}
