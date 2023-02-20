package com.mq.kafkaproducer.utils;

import com.mq.kafkaproducer.constants.OrderConstant;
import com.mq.kafkaproducer.dtos.request.OrderItemDetailDTO;
import com.mq.kafkaproducer.dtos.request.OrderRequestDTO;
import com.mq.kafkaproducer.models.Order;
import com.mq.kafkaproducer.models.OrderItemDetail;
import com.mq.kafkaproducer.models.Product;
import com.mq.kafkaproducer.repository.OrderItemDetailRepository;
import com.mq.kafkaproducer.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class OrderUtils {

    public static Double calculateTotalOrder(Double subTotal, Double tax) {
        return subTotal + tax;
    }

    public static OrderItemDetail getOrderDetails(Product orderProduct, OrderItemDetailDTO orderItemDetailDTO) {
        orderItemDetailDTO.setProductCost(orderProduct.getProductCost());
        orderItemDetailDTO.setQuantity(orderProduct.getQuantity());
        orderItemDetailDTO.setUnitPrice(calculateUnitPrice(orderProduct.getProductCost(), orderProduct.getQuantity()));
        orderItemDetailDTO.setShippingCost(OrderConstant.SHIPPING_COST);
        orderItemDetailDTO.setSubTotal(calculateSubTotal(orderItemDetailDTO.getUnitPrice(), orderItemDetailDTO.getShippingCost()));

        return OrderItemDetail.builder()
                .product(orderProduct)
                .productCost(orderItemDetailDTO.getProductCost())
                .shippingCost(orderItemDetailDTO.getShippingCost())
                .quantity(orderItemDetailDTO.getQuantity())
                .unitPrice(orderItemDetailDTO.getUnitPrice())
                .subTotal(orderItemDetailDTO.getSubTotal())
                .build();
    }

    private static Double calculateUnitPrice(Double productCost, Integer quantity) {
        return productCost * quantity;
    }

    private static Double calculateSubTotal(Double unitPrice, Double shippingCost) {
        return unitPrice * shippingCost;
    }
   
    

}
