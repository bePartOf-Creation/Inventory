package com.mq.kafkaconsumer.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderReport {
    private LocalDate date;
    private int totalOrders;
    private BigDecimal totalOrderAmount;

}
