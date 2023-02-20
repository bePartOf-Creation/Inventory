package com.mq.kafkaconsumer.services.orderLogs;

import com.mq.kafkaconsumer.controller.OrderdetailLogController;
import com.mq.kafkaconsumer.dtos.response.OrderReport;
import com.mq.kafkaconsumer.models.OrderDetailLog;
import com.mq.kafkaconsumer.repository.OrderDetailLogRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
class OrderDetailLogServiceImplTest {

    @Mock
    private final OrderDetailLogServiceImpl orderDetailLogService;


    @Test
    void getSalesReport() {
        // Mock the repository and data
        List<OrderDetailLog> orders = new ArrayList<>();

        OrderDetailLog order1 = new OrderDetailLog();
        order1.setId(1L);
        order1.setOrderDate(LocalDate.of(2022, 2, 15));
        order1.setOrderPrice(100.0);

        OrderDetailLog order2 = new OrderDetailLog();
        order2.setId(2L);
        order2.setOrderDate(LocalDate.of(2022, 2, 16));
        order2.setOrderPrice(50.0);

        orders.add(order1);
        orders.add(order2);

        Page<OrderDetailLog> orderDetailLogPage = new PageImpl<>(orders);
        Pageable pageable = PageRequest.of(0,2);

       List<OrderReport> reports = new ArrayList<>();

       OrderReport report1 = OrderReport.builder()
               .date(orderDetailLogPage.getContent().get(0).getOrderDate())
               .totalOrders(1)
               .totalOrderAmount(BigDecimal.valueOf(100.0))
               .build();
        OrderReport report2 = OrderReport.builder()
                .date(orderDetailLogPage.getContent().get(1).getOrderDate())
                .totalOrders(1)
                .totalOrderAmount(BigDecimal.valueOf(50.0))
                .build();

       reports.add(report1);
       reports.add(report2);
        // Call the method
        when(orderDetailLogService.getSalesReport(
                LocalDate.of(2022, 2, 15),
                LocalDate.of(2022, 2, 16), pageable)
        ).thenReturn(reports);

        // Verify the results
        assertEquals(2, reports.size());
        OrderReport orderReport1 = reports.get(0);
        assertEquals(LocalDate.of(2022, 2, 15), orderReport1.getDate());
        assertEquals(1, orderReport1.getTotalOrders());
        assertEquals(BigDecimal.valueOf(100.0), orderReport1.getTotalOrderAmount());

        OrderReport orderReport2 = reports.get(1);
        assertEquals(LocalDate.of(2022, 2, 16), orderReport2.getDate());
        assertEquals(1, orderReport2.getTotalOrders());
        assertEquals(BigDecimal.valueOf(50.0), orderReport2.getTotalOrderAmount());
    }

}