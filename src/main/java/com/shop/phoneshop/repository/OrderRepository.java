package com.shop.phoneshop.repository;

import com.shop.phoneshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT COALESCE(SUM(o.finalPrice), 0) FROM Order o WHERE o.status = 'PAID'")
    Long getTotalSales();
}
