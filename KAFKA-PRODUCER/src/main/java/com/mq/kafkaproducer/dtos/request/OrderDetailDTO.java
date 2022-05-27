package com.mq.kafkaproducer.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class OrderDetailDTO {

    private Integer quantity;
    private Double unitPrice;
    private Double subTotal;
    private Double productCost;
    private Double shippingCost;
}
