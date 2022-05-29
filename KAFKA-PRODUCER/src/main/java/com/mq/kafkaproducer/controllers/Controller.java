package com.mq.kafkaproducer.controllers;

import com.mq.kafkaproducer.builder.MessageQueueBuilder;
import com.mq.kafkaproducer.dtos.genericResponse.GenericOrderResponseMapper;
import com.mq.kafkaproducer.dtos.request.OrderDetailDTO;
import com.mq.kafkaproducer.dtos.request.ProductDTO;
import com.mq.kafkaproducer.dtos.response.ApiResponse;
import com.mq.kafkaproducer.models.Order;
import com.mq.kafkaproducer.models.Product;
import com.mq.kafkaproducer.services.ProductServiceImpl;
import com.mq.kafkaproducer.services.PushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.KafkaException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("sales")
public class Controller {

    private final Logger log = LoggerFactory.getLogger(Controller.class);
    @Autowired
    private PushService mNotificationService;

    @Autowired
    private ProductServiceImpl productService;

//    public static final String TOPIC = "kafka_topic_Q";

    @CrossOrigin
    @PostMapping("/new_product")
    public ResponseEntity<?> createNewProduct(@RequestBody final ProductDTO productDTO) {
        log.info("::: Creating New Product.....");
        Product newProduct = this.productService.createANewProduct(productDTO);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @CrossOrigin
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody final ProductDTO productDTO, @PathVariable Long id) {
        log.info("::: Update an Existing Product.....");
        try {
            Product newProduct = this.productService.updateProduct(productDTO, id);
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @CrossOrigin
    @PostMapping("/order/{customerId}/{productId}")
    public Order sendMessage(@RequestBody final OrderDetailDTO orderDetailsDTO, @PathVariable Long customerId, @PathVariable Long productId) {

        log.info("::: Sending Order message to KafkaConsumer.....");
        if (orderDetailsDTO == null && customerId == null && productId == null) {
            throw new KafkaException("::: Message failed to send :::");
        }

        try {
            Order orderReports = this.productService.placeOrder(customerId,productId,orderDetailsDTO);
            //Generate OrderPayload To Consumer
            GenericOrderResponseMapper orderPayloadToQueue = MessageQueueBuilder.generatePayloadForQueue(orderReports);
            
            //Push OrderPayload To Queue
            mNotificationService.push(orderPayloadToQueue);
            log.info(":::Order Payload sent To Queue successfully :::");

            return orderReports;
        } catch (Exception ex) {
            throw new KafkaException("::: Message failed to send :::");
        }

    }
}
