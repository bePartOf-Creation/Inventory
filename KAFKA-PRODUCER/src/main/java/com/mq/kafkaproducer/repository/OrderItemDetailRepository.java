package com.mq.kafkaproducer.repository;

import com.mq.kafkaproducer.models.OrderItemDetail;
import com.mq.kafkaproducer.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemDetailRepository extends JpaRepository<OrderItemDetail,Long> {

    List<OrderItemDetail> findOrderItemDetailsByProduct(Product  product);
}
