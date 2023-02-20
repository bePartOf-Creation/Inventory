package com.mq.kafkaconsumer.builders;

import com.mq.kafkaconsumer.constants.ReportConstant;
import com.mq.kafkaconsumer.dtos.response.GenericResponseBody;
import com.mq.kafkaconsumer.dtos.response.OrderReport;

import java.util.List;

public class ResponseBuilder {

    public static GenericResponseBody orderReportResponse(List<OrderReport> reports){
        return GenericResponseBody.builder()
                .status(true)
                .message(ReportConstant.RESPONSE_MESSAGE)
                .data(reports)
                .build();
    }
}
