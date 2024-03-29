package com.mq.kafkaconsumer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "orderDetails")
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class OrderDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;
    private Double productCost;
    private String name;
    private String shortDescription;
    private Double productPrice;
    private LocalDate productCreatedDate;
    
    @ManyToOne
    @JoinColumn(name = "report_id")
    private OrderDetailLog orderReport;
}
