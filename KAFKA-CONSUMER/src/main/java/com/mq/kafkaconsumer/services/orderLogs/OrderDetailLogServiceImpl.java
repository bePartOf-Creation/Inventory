package com.mq.kafkaconsumer.services;

import com.mq.kafkaconsumer.builders.EntityBuilder;
import com.mq.kafkaconsumer.models.OrderDetailLog;
import com.mq.kafkaconsumer.repository.OrderDetailLogRepository;
import com.mq.kafkaconsumer.dtos.response.GenericOrderResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OrderDetailLogServiceImpl implements OrderDetailLogService {
    @Autowired
    private OrderDetailLogRepository orderDetailRepo;
    
    @Override
    public OrderDetailLog createSaleReport(GenericOrderResponseMapper payloadFromQueue) {
        OrderDetailLog newOrderReport = EntityBuilder.orderDetailReportBuild(payloadFromQueue);
        return orderDetailRepo.save(newOrderReport);
    }

    @Override
    public Page<OrderDetailLog> getSalesReport(String startDate, String endDate) {
        LocalDate orderStartDate = LocalDate.parse(startDate);
        LocalDate orderEndDate = LocalDate.parse(endDate);
        Pageable pageable = PageRequest.of(0,5);

        return this.orderDetailRepo.findOrderDetailReportsByOrderDateBetween(orderStartDate,orderEndDate,pageable);
    }
}
