package com.shop.phoneshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phone_id")
    private Phone phone;

    private int quantity;

    private int originalPrice;
    private int discountPrice;
    private int finalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime paidAt;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    // 결제 완료 처리
    public void markPaid() {
        this.status = OrderStatus.PAID;
        this.paidAt = LocalDateTime.now();
    }

    // 취소 처리
    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    public void updatePrice(int originalPrice, int discountPrice, int finalPrice) {
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.finalPrice = finalPrice;
    }

    // 쿠폰 적용 최종 결제 금액 최신화
    public void updateFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }


}
