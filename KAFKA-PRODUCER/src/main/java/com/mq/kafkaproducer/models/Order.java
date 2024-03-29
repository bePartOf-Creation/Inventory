package com.mq.kafkaproducer.models;

import com.mq.kafkaproducer.constants.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate orderDate;
    private Double shippingCost;
    private String phoneNumber;
    private Double tax;
//    private Double orderPrice;
//    private Double productCost;
    private Double subTotal;
    private Double total;
    private String customerAddress;
    private String customerName;
    private String customerPhoneNumber;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
//    @ManyToOne
//    @JoinColumn(name = "customer_id")
//    private Customer orderingCustomer;
    
    @OneToMany(mappedBy = "orders")
    private List<OrderItemDetail> orderItemDetails = new ArrayList<>();
    
  
    
}
