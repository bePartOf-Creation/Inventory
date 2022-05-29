package com.mq.kafkaproducer.repository;

import com.mq.kafkaproducer.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
}
