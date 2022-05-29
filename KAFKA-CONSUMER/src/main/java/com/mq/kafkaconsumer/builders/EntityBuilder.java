package com.mq.kafkaconsumer.builders;

import com.mq.kafkaconsumer.models.OrderDetailReport;
import com.mq.kafkaconsumer.response.genericResponse.GenericOrderResponseMapper;

public class EntityBuilder {
    
    public static OrderDetailReport orderDetailReportBuild(GenericOrderResponseMapper payloadFromQueue){
         final String customerOrderAddress = payloadFromQueue.getMessage().getOrderAddress();
         final String orderingCustomer = payloadFromQueue.getMessage().getCustomerKycInfo().getFirstName();
        
        
        return OrderDetailReport.builder()
                .customerAddress(customerOrderAddress)
                .orderDetails(payloadFromQueue.getMessage().getOrderDetails())
                .orderPrice(payloadFromQueue.getMessage().getOrderPrice())
                .orderTime(payloadFromQueue.getMessage().getOrderTime())
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
