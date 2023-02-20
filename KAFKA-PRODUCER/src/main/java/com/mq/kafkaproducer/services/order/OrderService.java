package com.mq.kafkaproducer.services.order;

import com.mq.kafkaproducer.dtos.request.OrderRequestDTO;
import com.mq.kafkaproducer.models.Order;

public interface OrderService {
    Order placeOrder(OrderRequestDTO orderRequestDTO);
}
