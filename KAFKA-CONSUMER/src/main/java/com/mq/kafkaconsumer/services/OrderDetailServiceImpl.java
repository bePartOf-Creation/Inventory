package com.mq.kafkaconsumer.services;

import com.mq.kafkaconsumer.builders.EntityBuilder;
import com.mq.kafkaconsumer.models.OrderDetailReport;
import com.mq.kafkaconsumer.repository.OrderDetailRepository;
import com.mq.kafkaconsumer.response.genericResponse.GenericOrderResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OrderDetailServiceImpl implements OrderDetailService{
    @Autowired
    private OrderDetailRepository orderDetailRepo;
    
    @Override
    public OrderDetailReport createSaleReport(GenericOrderResponseMapper payloadFromQueue) {
        OrderDetailReport newOrderReport = EntityBuilder.orderDetailReportBuild(payloadFromQueue);
        return orderDetailRepo.save(newOrderReport);
    }

    @Override
    public Page<OrderDetailReport> getSalesReport(String startDate, String endDate) {
        LocalDate orderStartDate = LocalDate.parse(startDate);
        LocalDate orderEndDate = LocalDate.parse(endDate);
        Pageable pageable = PageRequest.of(0,5);

        return this.orderDetailRepo.findOrderDetailReportsByOrderDateBetween(orderStartDate,orderEndDate,pageable);
    }
}
