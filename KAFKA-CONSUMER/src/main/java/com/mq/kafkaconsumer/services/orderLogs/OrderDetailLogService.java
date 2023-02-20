package com.mq.kafkaconsumer.services.orderLogs;

import com.mq.kafkaconsumer.dtos.response.OrderReport;
import com.mq.kafkaconsumer.dtos.response.GenericOrderResponseMapper;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface OrderDetailLogService {
    
    void createSaleReport(GenericOrderResponseMapper payloadFromQueue);
    List<OrderReport> getSalesReport(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
