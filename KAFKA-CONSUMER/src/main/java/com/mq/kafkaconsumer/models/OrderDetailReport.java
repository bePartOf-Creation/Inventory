package com.mq.kafkaconsumer.models;


import com.mq.kafkaconsumer.constants.PaymentMethod;

import java.time.LocalDateTime;
import java.util.Set;

public class OrderDetailReport {
    private LocalDateTime orderTime;
    private Double shippingCost;
    private String phoneNumber;
    private Double tax;
    private Double orderPrice;
    private Double productCost;
    private Double subTotal;
    private Double total;

    private Address customerAddress;


    private PaymentMethod paymentMethod;


    private Customer orderingCustomer;


    private Set<OrderDetails> orderDetails;
}
