package com.mq.kafkaproducer.builder;

import com.mq.kafkaproducer.constants.OrderConstant;
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
                .emailAddress(order.getOrderingCustomer().getEmailAddress())
                .firstName(order.getOrderingCustomer().getFirstName())
                .lastName(order.getOrderingCustomer().getLastName())
                .phoneNumber(order.getPhoneNumber())
                .build();
        
        GenericOrderResponse genericOrderResponse = GenericOrderResponse.builder()
                .actionType(OrderConstant.RESPONSE_TYPE)
                .orderDate(order.getOrderDate())
                .orderPrice(order.getOrderPrice())
                .phoneNumber(order.getPhoneNumber())
                .productCost(order.getProductCost())
                .shippingCost(order.getShippingCost())
                .orderAddress(order.getCustomerAddress())
                .paymentMethod(order.getPaymentMethod().name())
                .orderDetails(order.getOrderDetails())
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
