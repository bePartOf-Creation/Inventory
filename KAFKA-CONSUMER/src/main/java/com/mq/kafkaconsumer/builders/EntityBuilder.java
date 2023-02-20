package com.mq.kafkaconsumer.builders;

import com.mq.kafkaconsumer.dtos.response.GenericOrderItem;
import com.mq.kafkaconsumer.models.OrderDetailLog;
import com.mq.kafkaconsumer.dtos.response.GenericOrderResponseMapper;
import com.mq.kafkaconsumer.models.OrderDetails;

import java.util.ArrayList;
import java.util.List;

public class EntityBuilder {
    
    public static OrderDetailLog orderDetailReportBuild(GenericOrderResponseMapper payloadFromQueue){
         final String customerOrderAddress = payloadFromQueue.getMessage().getOrderAddress();
         final String orderingCustomer = payloadFromQueue.getMessage().getCustomerKycInfo().getFirstName();
         final List<GenericOrderItem> itemsInOrderFromQueue = payloadFromQueue.getMessage().getItemsInOrder();

         List<OrderDetails> items = new ArrayList<>();
         for (GenericOrderItem item: itemsInOrderFromQueue) {
                OrderDetails orderDetails = OrderDetails.builder()
                        .name(item.getName())
                        .productCost(item.getProductCost())
                        .productCreatedDate(item.getProductCreatedDate())
                        .productPrice(item.getProductPrice())
                        .quantity(item.getQuantity())
                        .shortDescription(item.getShortDescription()).build();
                items.add(orderDetails);
         }

        return OrderDetailLog.builder()
                .customerAddress(customerOrderAddress)
                .orderPrice(payloadFromQueue.getMessage().getOrderPrice())
                .orderDetails(items)
                .orderDate(payloadFromQueue.getMessage().getOorderDate())
                .orderingCustomer(orderingCustomer)
                .paymentMethod(payloadFromQueue.getMessage().getPaymentMethod())
                .phoneNumber(payloadFromQueue.getMessage().getPhoneNumber())
                .productCost(payloadFromQueue.getMessage().getProductCost())
                .shippingCost(payloadFromQueue.getMessage().getShippingCost())
                .subTotal(payloadFromQueue.getMessage().getSubTotal())
                .total(payloadFromQueue.getMessage().getTotal())
                .tax(payloadFromQueue.getMessage().getTax())
                .build();
    }
}
