package com.mq.kafkaconsumer.services;

import com.mq.kafkaconsumer.config.KafkaConfig;
import com.mq.kafkaconsumer.constants.InventoryConstant;
import com.mq.kafkaconsumer.models.MessageBody;
import com.mq.kafkaconsumer.response.genericResponse.GenericOrderResponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    private final Logger log = LoggerFactory.getLogger(MessageConsumer.class);

    @KafkaListener(topics = KafkaConfig.TOPIC, groupId = KafkaConfig.GROUP_ID,
            containerFactory = "getKafkaListenerContainerFactory")
    public void consumer(@Payload GenericOrderResponseMapper payloadFromQueue) {

        System.out.println("::::::::::Received To Payload from the Producer "+ payloadFromQueue);
        log.info("::: Receiving Payload....");

        if (payloadFromQueue == null) {
            throw new KafkaException("::: Receiving Message failed....");
        }

        try {
            System.out.println("Message received has body: " + payloadFromQueue);
            /*
            Generate Sales Inventory Report Payload from queue;
             */
            getOrderDetailForSalesReportFromQueue(payloadFromQueue);
        } catch (Exception ex) {
            throw new KafkaException("::: Receiving Message Failed....");
        }
    }
    
    
    
    public void getOrderDetailForSalesReportFromQueue(GenericOrderResponseMapper payloadFromQueue){
        switch(payloadFromQueue.getMessage().getActionType()){
            case InventoryConstant.RESPONSE_TYPE:
                //:TODO Generate Inventory Report.
        }
    }
}
