package com.mq.kafkaproducer.builder;

import com.mq.kafkaproducer.dtos.genericResponse.GenericCustomerKycResponse;
import com.mq.kafkaproducer.dtos.genericResponse.GenericOrderResponse;
import com.mq.kafkaproducer.dtos.genericResponse.GenericOrderResponseMapper;
import com.mq.kafkaproducer.models.Order;

/**
 * The type Message queue builder.
 */
public class MessageQueueBuilder {

    public static GenericOrderResponseMapper generatePayloadForQueue(Order order) {
        GenericCustomerKycResponse customerOrderResponseKycResponse = GenericCustomerKycResponse.builder()
                .paymentMethod(order.getPaymentMethod())
                .customerAddress(order.getCustomerAddress())
                .orderDetails(order.getOrderDetails())
                .orderingCustomer(order.getOrderingCustomer())
                .build();
        
        GenericOrderResponse genericOrderResponse = GenericOrderResponse.builder()
                .orderTime(order.getOrderTime())
                .orderPrice(order.getOrderPrice())
                .phoneNumber(order.getPhoneNumber())
                .productCost(order.getProductCost())
                .shippingCost(order.getShippingCost())
                .subTotal(order.getSubTotal())
                .tax(order.getTax())
                .total(order.getTotal())
                .customerKycInfo(customerOrderResponseKycResponse)
                .build();
        
        return GenericOrderResponseMapper.builder()
                        .message(genericOrderResponse)
                        .build();
    }
}
