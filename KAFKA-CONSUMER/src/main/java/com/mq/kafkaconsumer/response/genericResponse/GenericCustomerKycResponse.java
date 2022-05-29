package com.mq.kafkaconsumer.response.genericResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mq.kafkaproducer.constants.PaymentMethod;
import com.mq.kafkaproducer.models.Address;
import com.mq.kafkaproducer.models.Customer;
import com.mq.kafkaproducer.models.OrderDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericCustomerKycResponse {

    private Address customerAddress;
    private PaymentMethod paymentMethod;
    private Customer orderingCustomer;
    private Set<OrderDetails> orderDetails;
}
