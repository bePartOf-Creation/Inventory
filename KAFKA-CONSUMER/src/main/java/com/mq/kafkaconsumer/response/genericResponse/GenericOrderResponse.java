package com.mq.kafkaconsumer.response.genericResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mq.kafkaconsumer.models.OrderDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericOrderResponse {
    
    private LocalDate orderTime;
    private String actionType;
    private Double shippingCost;
    private String phoneNumber;
    private Double tax;
    private Double orderPrice;
    private Double productCost;
    private Double subTotal;
    private Double total;
    private String orderAddress;
    private String paymentMethod;
    private GenericCustomerKycResponse customerKycInfo;
    private Set<OrderDetails> orderDetails;
}
