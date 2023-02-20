package com.mq.kafkaproducer.services.product;

import com.mq.kafkaproducer.dtos.request.ProductDTO;
import com.mq.kafkaproducer.models.Product;
import org.springframework.data.domain.Page;

public interface ProductService {
    Product createANewProduct(ProductDTO productDTO);
    Product updateProduct(ProductDTO productDTO, Long id);
    Page<Product> listAllProducts(int pageSize, int pageNumber);
    Product getProductById(long l);
}
