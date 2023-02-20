package com.mq.kafkaproducer.dtos.genericResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mq.kafkaproducer.models.OrderItemDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericOrderResponse {
    
    private LocalDate orderDate;
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
    private List<GenericOrderItem> itemsInOrder;

}
