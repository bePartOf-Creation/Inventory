package com.mq.kafkaconsumer.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "orderDetails")
@AllArgsConstructor
@Data
@NoArgsConstructor
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
    @JoinColumn(name = "report_id")
    private OrderDetailReport orderReport;
    
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
