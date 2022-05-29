package com.mq.kafkaconsumer.repository;

import com.mq.kafkaconsumer.models.OrderDetailReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailReport,Long> {
    
    Page<OrderDetailReport> findOrderDetailReportsByOrderDateBetween(LocalDate orderDate, LocalDate orderDate2, Pageable pageable);
}
