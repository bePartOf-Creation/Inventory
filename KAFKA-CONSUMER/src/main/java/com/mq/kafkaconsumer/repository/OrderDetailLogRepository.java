package com.mq.kafkaconsumer.repository;

import com.mq.kafkaconsumer.models.OrderDetailLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface OrderDetailLogRepository extends JpaRepository<OrderDetailLog,Long> {

//    Page<OrderDetailLog> findOrderDetailReportsByOrderDateBetween(LocalDate orderDate, LocalDate orderDate2, Pageable pageable);

//    Page<OrderDetailLog> findByOrderDateBetween(LocalDate atStartOfDay, LocalDate atStartOfDay1, Pageable pageable);

    Page<OrderDetailLog> findByOrderDateBetween(LocalDateTime atStartOfDay, LocalDateTime atStartOfDay1, Pageable pageable);
}
