package com.shop.phoneshop.dto;

import com.shop.phoneshop.model.Order;
import com.shop.phoneshop.model.OrderItem;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderHistoryResponse {

    private final Long orderId;
    private final String status;
    private final int finalPrice;
    private final LocalDateTime createdAt;
    private final LocalDateTime paidAt;
    private final List<OrderHistoryItemResponse> items;

    @Builder
    private OrderHistoryResponse(Long orderId,
                                 String status,
                                 int finalPrice,
                                 LocalDateTime createdAt,
                                 LocalDateTime paidAt,
                                 List<OrderHistoryItemResponse> items) {
        this.orderId = orderId;
        this.status = status;
        this.finalPrice = finalPrice;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
        this.items = items;
    }

    public static OrderHistoryResponse from(Order order) {
        List<OrderHistoryItemResponse> items = order.getItems().stream()
                .map(OrderHistoryItemResponse::from)
                .collect(Collectors.toList());

        return OrderHistoryResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus() != null ? order.getStatus().name() : null)
                .finalPrice(order.getFinalPrice())
                .createdAt(order.getCreatedAt())
                .paidAt(order.getPaidAt())
                .items(items)
                .build();
    }

    @Getter
    @Builder
    public static class OrderHistoryItemResponse {

        private final Long phoneId;
        private final String phoneName;
        private final String brand;
        private final String imageUrl;
        private final int quantity;
        private final int unitPrice;
        private final int totalPrice;

        public static OrderHistoryItemResponse from(OrderItem orderItem) {
            int unitPrice = orderItem.getPhone().getPrice();
            int total = unitPrice * orderItem.getQuantity();

            return OrderHistoryItemResponse.builder()
                    .phoneId(orderItem.getPhone().getId())
                    .phoneName(orderItem.getPhone().getName())
                    .brand(orderItem.getPhone().getBrand())
                    .imageUrl(orderItem.getPhone().getImageUrl())
                    .quantity(orderItem.getQuantity())
                    .unitPrice(unitPrice)
                    .totalPrice(total)
                    .build();
        }
    }
}
