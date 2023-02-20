package com.mq.kafkaconsumer.services.orderLogs;

import com.mq.kafkaconsumer.builders.EntityBuilder;
import com.mq.kafkaconsumer.dtos.response.OrderReport;
import com.mq.kafkaconsumer.models.OrderDetailLog;
import com.mq.kafkaconsumer.repository.OrderDetailLogRepository;
import com.mq.kafkaconsumer.dtos.response.GenericOrderResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class OrderDetailLogServiceImpl implements OrderDetailLogService {

    private final OrderDetailLogRepository orderDetailRepo;
    
    @Override
    @Transactional
    public void createSaleReport(GenericOrderResponseMapper payloadFromQueue) {
        OrderDetailLog newOrderReport = EntityBuilder.orderDetailReportBuild(payloadFromQueue);
        orderDetailRepo.save(newOrderReport);
    }

    @Override
    public List<OrderReport> getSalesReport(LocalDate startDate, LocalDate endDate, Pageable pageable) {
//        LocalDate orderStartDate = LocalDate.parse(startDate);
//        LocalDate orderEndDate = LocalDate.parse(endDate);
//        Pageable pageable = PageRequest.of(0,5);
//
//        return this.orderDetailRepo.findOrderDetailReportsByOrderDateBetween(orderStartDate,orderEndDate,pageable);

        // Get all orders within the date range
       Page<OrderDetailLog> orders = this.orderDetailRepo.findByOrderDateBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay(),pageable);

        // Group the orders by date
        Map<LocalDate, List<OrderDetailLog>> ordersByDate = orders.getContent().stream()
                .collect(Collectors.groupingBy(OrderDetailLog::getOrderDate));

        List<OrderReport> orderReports = new ArrayList<>();

        // Calculate the total number of orders for this date
        ordersByDate.forEach((date, ordersForDate) -> {
            int totalOrders = ordersForDate.size();

            // Calculate the total order amount for this date
            double totalOrderAmount = ordersForDate.stream()
                    .mapToDouble(order -> order.getOrderDetails().stream()
                            .mapToDouble(itemInOrder -> itemInOrder.getProductPrice() * itemInOrder.getQuantity())
                            .sum())
                    .sum();

            // Add a new OrderReport object to the list
            orderReports.add(new OrderReport(date, totalOrders, BigDecimal.valueOf(totalOrderAmount)));
        });

        return orderReports;

    }


}
