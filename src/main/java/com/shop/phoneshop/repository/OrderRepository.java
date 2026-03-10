package com.shop.phoneshop.repository;

import com.shop.phoneshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT COALESCE(SUM(o.finalPrice), 0) FROM Order o WHERE o.status = 'PAID'")
    Long getTotalSales();

    @Query("SELECT DISTINCT o FROM Order o"
            + " LEFT JOIN FETCH o.items i"
            + " LEFT JOIN FETCH i.phone"
            + " WHERE o.user.id = :userId"
            + " AND o.status = 'PAID'"
            + " ORDER BY o.createdAt DESC")
    List<Order> findAllPaidWithItemsByUserId(@Param("userId") Long userId);
}
