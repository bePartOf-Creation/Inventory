package com.mq.kafkaproducer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_details")
public class OrderDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    private Double unitPrice;
    private Double subTotal;
    private Double productCost;
    private Double shippingCost;
    
    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Order orders;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
}
