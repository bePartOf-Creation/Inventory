package com.mq.kafkaproducer.services;

import com.mq.kafkaproducer.dtos.request.OrderDetailDTO;
import com.mq.kafkaproducer.dtos.request.ProductDTO;
import com.mq.kafkaproducer.models.Order;
import com.mq.kafkaproducer.models.Product;
import org.springframework.data.domain.Page;

public interface ProductService {
    Product createANewProduct(ProductDTO productDTO);
    Product updateProduct(ProductDTO productDTO, Long id);
    Page<Product> listAllProducts(int pageSize, int pageNumber);
    Order placeOrder(Long customerID, Long ProductId, OrderDetailDTO orderDetailDTO);
}
