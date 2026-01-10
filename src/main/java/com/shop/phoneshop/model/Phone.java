package com.shop.phoneshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;
    private int price;
    private String imageUrl;

    public Phone(String name, String brand, int price, String imageUrl) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
