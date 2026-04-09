package com.shop.phoneshop.service;

import com.shop.phoneshop.dto.CartItemAddRequest;
import com.shop.phoneshop.dto.CartItemDecreaseRequest;
import com.shop.phoneshop.dto.CartItemResponse;
import com.shop.phoneshop.dto.CartResponse;
import com.shop.phoneshop.model.*;
import com.shop.phoneshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    /// +버튼 API
    @Transactional
    public void addItem(Long userId, CartItemAddRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        // 장바구니 없으면 생성
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));

        Phone phone = phoneRepository.findById(request.getPhoneId())
                .orElseThrow(() -> new IllegalArgumentException("폰 없음"));

        cartItemRepository.findByCartAndPhone(cart, phone)
                .ifPresentOrElse(
                        // 이미 있으면 +1
                        cartItem -> cartItem.changeQuantity(
                                cartItem.getQuantity() + 1
                        ),
                        // 없으면 새로 추가 (1개)
                        () -> cartItemRepository.save(
                                CartItem.builder()
                                        .cart(cart)
                                        .phone(phone)
                                        .quantity(1)
                                        .build()
                        )
                );
    }

    /// -버튼 API
    @Transactional
    public void decreaseItem(Long userId, CartItemDecreaseRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("장바구니 없음"));

        Phone phone = phoneRepository.findById(request.getPhoneId())
                .orElseThrow(() -> new IllegalArgumentException("폰 없음"));

        CartItem cartItem = cartItemRepository.findByCartAndPhone(cart, phone)
                .orElseThrow(() -> new IllegalStateException("장바구니에 해당 상품 없음"));

        int currentQuantity = cartItem.getQuantity();

        if (currentQuantity <= 1) {
            // ⭐ 1 → 0 이 되면 자동 삭제
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.changeQuantity(currentQuantity - 1);
        }
    }

    /// 장바구니 조회
    @Transactional(readOnly = true)
    public CartResponse getMyCart(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 없음"));

        List<CartItemResponse> items = cart.getCartItems().stream()
                .map(cartItem -> CartItemResponse.from(cartItem, baseUrl))
                .toList();

        int totalPrice = items.stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();

        return CartResponse.builder()
                .items(items)
                .totalPrice(totalPrice)
                .build();
    }

    /// 장바구니 전체 상품 삭제
    @Transactional
    public void clearCart(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 없음"));

        cart.getCartItems().clear();
    }


}
