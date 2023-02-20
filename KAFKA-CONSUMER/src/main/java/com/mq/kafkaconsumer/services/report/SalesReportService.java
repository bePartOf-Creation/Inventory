package com.mq.kafkaconsumer.services.report;

import com.mq.kafkaconsumer.config.KafkaConfig;
import com.mq.kafkaconsumer.constants.ReportConstant;
import com.mq.kafkaconsumer.dtos.response.GenericOrderResponseMapper;
import com.mq.kafkaconsumer.services.orderLogs.OrderDetailLogServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class SalesReportService {

    private final OrderDetailLogServiceImpl orderDetailLogService;
    private final Logger log = LoggerFactory.getLogger(SalesReportService.class);


    @KafkaListener(topics = KafkaConfig.TOPIC, groupId = KafkaConfig.GROUP_ID,
            containerFactory = "getKafkaListenerContainerFactory")
    public void consumer(@Payload GenericOrderResponseMapper payloadFromQueue) {

        System.out.println("::::: Received To Payload from the Producer :::::::" + payloadFromQueue);
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


    /**
     * This method is scalable to handle multiple events by its ACTION_TYPE.X
     * @param payloadFromQueue .
     */
    public void getOrderDetailForSalesReportFromQueue(GenericOrderResponseMapper payloadFromQueue) {
        switch (payloadFromQueue.getMessage().getActionType()) {
            case ReportConstant.SALES_REPORT:
                this.orderDetailLogService.createSaleReport(payloadFromQueue);
                break;
        }
    }
}
