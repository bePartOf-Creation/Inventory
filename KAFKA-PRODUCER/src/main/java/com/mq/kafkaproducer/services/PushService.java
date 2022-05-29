package com.mq.kafkaproducer.services;

import com.mq.kafkaproducer.dtos.genericResponse.GenericOrderResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class PushService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic}")
    private String topic;

    @Async
    public void push(GenericOrderResponseMapper message) {

        final ListenableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(topic, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                System.out.println("Sent message=[" + message +
                                           "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=["
                                           + message + "] due to : " + ex.getMessage());
            }
        });
    }
}
