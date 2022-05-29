package com.mq.kafkaconsumer.services;

import com.mq.kafkaconsumer.models.OrderDetailReport;
import com.mq.kafkaconsumer.response.genericResponse.GenericOrderResponseMapper;
import org.springframework.data.domain.Page;

public interface OrderDetailService {
    
    OrderDetailReport createSaleReport(GenericOrderResponseMapper payloadFromQueue);
    Page<OrderDetailReport> getSalesReport(String startDate, String endDate);
}
