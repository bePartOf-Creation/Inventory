package com.mq.kafkaproducer.builder;

import com.mq.kafkaproducer.constants.OrderConstant;
import com.mq.kafkaproducer.dtos.genericResponse.GenericCustomerKycResponse;
import com.mq.kafkaproducer.dtos.genericResponse.GenericOrderItem;
import com.mq.kafkaproducer.dtos.genericResponse.GenericOrderResponse;
import com.mq.kafkaproducer.dtos.genericResponse.GenericOrderResponseMapper;
import com.mq.kafkaproducer.models.Order;
import com.mq.kafkaproducer.models.OrderItemDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Message queue builder.
 */
public class MessageQueueBuilder {

    public static GenericOrderResponseMapper generatePayloadForQueue(Order order) {

        List<GenericOrderItem> itemsInOrder = new ArrayList<>();
        for (OrderItemDetail itemDetail:  order.getOrderItemDetails()) {
            GenericOrderItem  genericOrderItem = GenericOrderItem.builder()
                    .name(itemDetail.getProduct().getName())
                    .productCost(itemDetail.getProduct().getProductCost())
                    .productCreatedDate(itemDetail.getProduct().getProductCreatedDate())
                    .productPrice(itemDetail.getUnitPrice())
                    .quantity(itemDetail.getQuantity())
                    .shortDescription(itemDetail.getProduct().getShortDescription())
                    .build();

            itemsInOrder.add(genericOrderItem);
        }

        GenericCustomerKycResponse customerOrderResponseKycResponse = GenericCustomerKycResponse.builder()
                .customerName(order.getCustomerName())
                .customerPhoneNumber(order.getCustomerPhoneNumber())
                .build();

        GenericOrderResponse genericOrderResponse = GenericOrderResponse.builder()
                .actionType(OrderConstant.RESPONSE_TYPE)
                .orderDate(order.getOrderDate())
                .phoneNumber(order.getPhoneNumber())
                .shippingCost(order.getShippingCost())
                .orderAddress(order.getCustomerAddress())
                .paymentMethod(order.getPaymentMethod().name())
                .itemsInOrder(itemsInOrder)
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
