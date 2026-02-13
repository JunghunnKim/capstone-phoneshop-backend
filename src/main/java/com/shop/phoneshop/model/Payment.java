package com.shop.phoneshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String tid;

    private String partnerOrderId;

    private String partnerUserId;

    private int amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;

    public void updateTid(String tid) {
        this.tid = tid;
    }

    public void markSuccess() {
        this.status = PaymentStatus.SUCCESS;
        this.approvedAt = LocalDateTime.now();
    }

    public void markFail() {
        this.status = PaymentStatus.FAIL;
    }
}
