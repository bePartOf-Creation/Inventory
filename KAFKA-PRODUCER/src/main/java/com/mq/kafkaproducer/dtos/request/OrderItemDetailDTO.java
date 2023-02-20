package com.mq.kafkaproducer.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDetailDTO {

    private Integer quantity;
    private Long productId;
    private Double unitPrice;
    private Double subTotal;
    private Double productCost;
    private Double shippingCost;
}
