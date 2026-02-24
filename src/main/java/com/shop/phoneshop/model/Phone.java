package com.shop.phoneshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String display;
    private String processor;
    private String ram;
    private String storage;
    private String battery;
    private String camera;

    public Phone(
            String name,
            String brand,
            int price,
            String imageUrl,
            String display,
            String processor,
            String ram,
            String storage,
            String battery,
            String camera
    ) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.imageUrl = imageUrl;
        this.display = display;
        this.processor = processor;
        this.ram = ram;
        this.storage = storage;
        this.battery = battery;
        this.camera = camera;
    }
}
