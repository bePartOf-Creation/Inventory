package com.mq.kafkaproducer.controllers;

import com.mq.kafkaproducer.builder.MessageQueueBuilder;
import com.mq.kafkaproducer.dtos.genericResponse.GenericOrderResponseMapper;
import com.mq.kafkaproducer.dtos.request.OrderRequestDTO;
import com.mq.kafkaproducer.dtos.request.ProductDTO;
import com.mq.kafkaproducer.dtos.response.ApiResponse;
import com.mq.kafkaproducer.models.Order;
import com.mq.kafkaproducer.models.Product;
import com.mq.kafkaproducer.services.order.OrderServiceImpl;
import com.mq.kafkaproducer.services.product.ProductServiceImpl;
import com.mq.kafkaproducer.services.PushService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.KafkaException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("sales")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductServiceImpl productService;


    @GetMapping("/products")
    public ResponseEntity<?> allProduct(@RequestParam("pageNumber") Integer pageNumber,
                                        @RequestParam("pageSize") Integer pageSize) {

        log.info("::: Creating New Product.....");
        Page<Product> products = this.productService.listAllProducts(pageNumber,pageSize);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/new_product")
    public ResponseEntity<?> createNewProduct(@RequestBody final ProductDTO productDTO) {
        log.info("::: Creating New Product.....");
        Product newProduct = this.productService.createANewProduct(productDTO);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }


    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody final ProductDTO productDTO, @PathVariable Long id) {
        log.info("::: Update an Existing Product.....");
        try {
            Product newProduct = this.productService.updateProduct(productDTO, id);
            return new ResponseEntity<>(newProduct, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}



