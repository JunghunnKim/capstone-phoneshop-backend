package com.shop.phoneshop.repository;

import com.shop.phoneshop.model.Payment;
import com.shop.phoneshop.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTid(String tid);
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = 'SUCCESS'")
    int sumTotalSales();

    Optional<Payment> findTopByStatusOrderByCreatedAtDesc(PaymentStatus status);

}
