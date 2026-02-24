package com.shop.phoneshop.service;

import com.shop.phoneshop.dto.KakaoPayRequest.OrderItemRequest;
import com.shop.phoneshop.dto.KakaoPayRequest.OrderRequest;
import com.shop.phoneshop.exception.EmptyOrderItemException;
import com.shop.phoneshop.exception.OrderNotFoundException;
import com.shop.phoneshop.exception.PhoneNotFoundException;
import com.shop.phoneshop.exception.UserNotFoundException;
import com.shop.phoneshop.model.*;
import com.shop.phoneshop.repository.OrderRepository;
import com.shop.phoneshop.repository.PhoneRepository;
import com.shop.phoneshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;

    /// 다중 상품 주문 생성
    public Order createOrder(OrderRequest request) {

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new EmptyOrderItemException();
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(UserNotFoundException::new);

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        int totalPrice = 0;

        for (OrderItemRequest itemRequest : request.getItems()) {

            Phone phone = phoneRepository.findById(itemRequest.getPhoneId())
                    .orElseThrow(PhoneNotFoundException::new);

            int itemTotalPrice = phone.getPrice() * itemRequest.getQuantity();

            OrderItem orderItem = OrderItem.builder()
                    .phone(phone)
                    .quantity(itemRequest.getQuantity())
                    .build();

            order.addItem(orderItem);
            totalPrice += itemTotalPrice;
        }

        // 가격 세팅
        order.updatePrice(totalPrice, 0, totalPrice);

        return orderRepository.save(order);
    }
}