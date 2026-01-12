package com.shop.phoneshop.repository;

import com.shop.phoneshop.model.Cart;
import com.shop.phoneshop.model.CartItem;
import com.shop.phoneshop.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndPhone(Cart cart, Phone phone);

    List<CartItem> findAllByCart(Cart cart);
}
