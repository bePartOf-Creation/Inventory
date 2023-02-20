package com.mq.kafkaconsumer.controller;

import com.mq.kafkaconsumer.builders.ResponseBuilder;
import com.mq.kafkaconsumer.dtos.response.OrderReport;
import com.mq.kafkaconsumer.services.orderLogs.OrderDetailLogServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class OrderdetailLogController {
    private final Logger log = LoggerFactory.getLogger(OrderdetailLogController.class);

    private final OrderDetailLogServiceImpl orderDetailLogService;



    @GetMapping("reports")
    public ResponseEntity<?> getOrdersByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber,pageSize);

       log.info("::: Generating Sales Report ::::: ");
       List<OrderReport> orderReports = this.orderDetailLogService.getSalesReport(startDate, endDate, pageable);
       return new ResponseEntity<>(ResponseBuilder.orderReportResponse(orderReports), HttpStatus.OK);

    }
}
