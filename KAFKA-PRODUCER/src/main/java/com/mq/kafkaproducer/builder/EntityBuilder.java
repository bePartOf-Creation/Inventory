package com.mq.kafkaproducer.builder;

import com.mq.kafkaproducer.dtos.request.CustomerDTO;
import com.mq.kafkaproducer.dtos.request.ProductDTO;
import com.mq.kafkaproducer.models.Customer;
import com.mq.kafkaproducer.models.Product;

import java.time.LocalDate;

public class EntityBuilder {
    
    public static Customer customerBuilder(CustomerDTO customerDto){
        return Customer.builder()
                .emailAddress(customerDto.getEmailAddress())
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .phoneNumber(customerDto.getPhoneNumber())
                
                .build();
    }
    
    public static Product productBuilder(ProductDTO productDTO){
        return Product.builder()
                .productCost(productDTO.getProductCost())
                .productPrice(productDTO.getProductPrice())
                .name(productDTO.getName())
                .quantity(productDTO.getQuantity())
                .shortDescription(productDTO.getShortDescription())
                .productCreatedDate(LocalDate.now())
                .build();
    }
  
//   ' public static Order orderBuilder(OrderDTO orderDTO){
//        return Order.builder()
//                .addressLine1(orderDTO.getAddressLine1())
//                .orderPrice(orderDTO.getOrderPrice())
//                .orderTime(LocalDateTime.now())
//                .productCost(orderDTO.getProductCost())
//                .phoneNumber(orderDTO)
//                .shippingCost(orderDTO.getShippingCost())
//                .subTotal(orderDTO.getSubTotal())
//                .tax(orderDTO.getTax())
//                .total(orderDTO.getTotal())
//                .build();
//        
//    }'
}
