package com.mq.kafkaproducer.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CustomerDTO {
    private String firstName;
    private String LastName;
    private String emailAddress;
    private String phoneNumber;
}
