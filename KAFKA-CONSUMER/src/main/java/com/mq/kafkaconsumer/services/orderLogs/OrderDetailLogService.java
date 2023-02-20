package com.mq.kafkaconsumer.services;

import com.mq.kafkaconsumer.models.OrderDetailLog;
import com.mq.kafkaconsumer.dtos.response.GenericOrderResponseMapper;
import org.springframework.data.domain.Page;

public interface OrderDetailLogService {
    
    OrderDetailLog createSaleReport(GenericOrderResponseMapper payloadFromQueue);
    Page<OrderDetailLog> getSalesReport(String startDate, String endDate);
}
