package com.mq.kafkaproducer.dtos.request;

import com.mq.kafkaproducer.models.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private String customerName;
    private String phoneNumber;
    private Address customerAddress;
    private List<OrderItemDetailDTO> items;
}
