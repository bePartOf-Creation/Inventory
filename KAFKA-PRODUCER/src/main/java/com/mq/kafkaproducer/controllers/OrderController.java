package com.mq.kafkaproducer.controllers;

import com.mq.kafkaproducer.builder.MessageQueueBuilder;
import com.mq.kafkaproducer.dtos.genericResponse.GenericOrderResponseMapper;
import com.mq.kafkaproducer.dtos.request.OrderRequestDTO;
import com.mq.kafkaproducer.models.Order;
import com.mq.kafkaproducer.services.PushService;
import com.mq.kafkaproducer.services.order.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.KafkaException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class OrderController {
    private final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderServiceImpl orderService;
    private final PushService mNotificationService;


    @PostMapping("order")
    public ResponseEntity<String> placOrder(@RequestBody final OrderRequestDTO orderRequestDTO) {

        log.info("::: Sending Order message to KafkaConsumer.....");
        if (orderRequestDTO == null) {
            throw new KafkaException("::: Message failed to send :::");
        }

        try {
            Order orderReports = this.orderService.placeOrder(orderRequestDTO);
            //Generate OrderPayload To Consumer
            GenericOrderResponseMapper orderPayloadToQueue = MessageQueueBuilder.generatePayloadForQueue(orderReports);

            // Publish the OrderPayLoadQueue of the created Order to Kafka for reporting
            mNotificationService.push(orderPayloadToQueue);
            log.info(":::Order Payload sent To Queue successfully :::");

        } catch (Exception ex) {
            throw new KafkaException("::: Message failed to send :::");
        }

        return ResponseEntity.ok("Order placed successfully.");
    }

}
