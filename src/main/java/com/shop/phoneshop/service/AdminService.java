package com.shop.phoneshop.service;

import com.shop.phoneshop.repository.OrderRepository;
import com.shop.phoneshop.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final OrderRepository orderRepository;

    public Long getTotalSales() {
        return orderRepository.getTotalSales();
    }
}

