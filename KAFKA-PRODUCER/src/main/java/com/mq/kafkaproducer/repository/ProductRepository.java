package com.mq.kafkaproducer.repository;

import com.mq.kafkaproducer.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Query("select p from Product p WHERE p.name = ?1")
    Optional<Product> findByName(String name);
    
    Optional<Product> findById(Long id);
    Page<Product> findAll(Pageable pageable);
}
