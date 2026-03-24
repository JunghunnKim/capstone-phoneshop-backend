package com.shop.phoneshop.repository;

import com.shop.phoneshop.model.Cart;
import com.shop.phoneshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);

    void deleteByUser(User user);
}
