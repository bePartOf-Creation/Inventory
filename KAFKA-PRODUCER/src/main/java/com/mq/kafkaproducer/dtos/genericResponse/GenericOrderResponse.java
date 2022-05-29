package com.mq.kafkaproducer.dtos.genericResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericOrderResponse {
    
    private LocalDateTime orderTime;
    private Double shippingCost;
    private String phoneNumber;
    private Double tax;
    private Double orderPrice;
    private Double productCost;
    private Double subTotal;
    private Double total;
    private GenericCustomerKycResponse customerKycInfo;
}
