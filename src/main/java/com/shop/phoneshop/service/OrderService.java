package com.shop.phoneshop.service;

import com.shop.phoneshop.model.*;
import com.shop.phoneshop.repository.OrderRepository;
import com.shop.phoneshop.repository.PhoneRepository;
import com.shop.phoneshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shop.phoneshop.dto.KakaoPayRequest.*;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;

    /// 다중 상품 주문 생성

    @Transactional
    public Order createOrder(OrderRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        int totalPrice = 0;

        for (OrderItemRequest itemRequest : request.getItems()) {

            Phone phone = phoneRepository.findById(itemRequest.getPhoneId())
                    .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

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

    /// 결제 완료 처리
    public void markOrderPaid(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        order.markPaid();
    }

    /// 주문 취소
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        order.cancel();
    }
}
