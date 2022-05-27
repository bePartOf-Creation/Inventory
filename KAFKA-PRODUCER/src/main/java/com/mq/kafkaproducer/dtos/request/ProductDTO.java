package com.mq.kafkaproducer.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    
    private String name;
    private String shortDescription;
    private Integer quantity;
    private Integer productPrice;
    private Integer productCost;
    
    
}
