package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class Payload {
    private String order_id;
//    private String users_id;
    private String product_id;
    private int qty;
    private int unit_price;
    private int total_price;

    public Payload(OrderDto orderDto) {
        this.order_id = orderDto.getOrderId();
//        this.users_id = orderDto.getUserId();
        this.product_id = orderDto.getProductId();
        this.qty = orderDto.getQty();
        this.unit_price = orderDto.getUnitPrice();
        this.total_price = orderDto.getTotalPrice();
    }
}
