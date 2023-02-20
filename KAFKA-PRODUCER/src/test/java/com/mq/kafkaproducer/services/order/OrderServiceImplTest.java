package com.mq.kafkaproducer.services.order;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.mq.kafkaproducer.controllers.OrderController;
import com.mq.kafkaproducer.dtos.request.OrderItemDetailDTO;
import com.mq.kafkaproducer.dtos.request.OrderRequestDTO;
import com.mq.kafkaproducer.dtos.request.ProductDTO;
import com.mq.kafkaproducer.models.Order;
import com.mq.kafkaproducer.models.OrderItemDetail;
import com.mq.kafkaproducer.models.Product;
import com.mq.kafkaproducer.repository.OrderRepository;
import com.mq.kafkaproducer.services.product.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.JsonDeserializer.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES;

@SpringBootTest
@EmbeddedKafka
@RunWith(MockitoJUnitRunner.class)
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
class OrderServiceImplTest {


    @Mock
    private final ProductServiceImpl productService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private final EmbeddedKafkaBroker embeddedKafkaBroker;

    private static final String TOPIC = "kafka-topic-kmq";

    @InjectMocks
    private OrderServiceImpl orderService;


    @Test
    public void testPlaceOrderSuccess() {
        // create test data
        Product product = new Product(1L, "Hulu Boxer","Boxers for men",
                3,1500.0,1000.0, LocalDate.now());
        when(productService.getProductById(1L)).thenReturn(product);

        OrderRequestDTO orderRequest = new OrderRequestDTO();
        orderRequest.setCustomerName("Bolatito");
        OrderItemDetailDTO orderLineItemRequest = new OrderItemDetailDTO();
        orderLineItemRequest.setProductId(1L);
        orderLineItemRequest.setQuantity(3);
        orderRequest.setItems(Collections.singletonList(orderLineItemRequest));

        // create expected order and order line item objects
        Order expectedOrder = new Order();
        expectedOrder.setCustomerName("Bolatito");
        OrderItemDetail expectedLineItem = new OrderItemDetail();
        expectedLineItem.setProduct(product);
        expectedLineItem.setQuantity(3);
        expectedOrder.setOrderItemDetails(Collections.singletonList(expectedLineItem));

        // mock repository method
        when(orderRepository.save(any(Order.class))).thenReturn(expectedOrder);

        // test the method
        Order actualOrder = orderService.placeOrder(orderRequest);

        // assert the result
        assertNotNull(actualOrder);
        assertEquals(expectedOrder.getCustomerName(),actualOrder.getCustomerName());
        assertEquals(expectedOrder.getOrderItemDetails().size(), actualOrder.getOrderItemDetails().size());
        assertEquals(expectedOrder.getOrderItemDetails().get(0).getProduct().getId(), actualOrder.getOrderItemDetails().get(0).getProduct().getId());
        assertEquals(expectedOrder.getOrderItemDetails().get(0).getQuantity(), actualOrder.getOrderItemDetails().get(0).getQuantity());
    }

    @Test
    public void testPlaceOrderProductOutOfStock() {
        // create test data
        Product product = new Product(1L, "Hulu Boxer","Boxers for men",
                5,1500.0,1000.0, LocalDate.now());

        when(productService.getProductById(1L)).thenReturn(product);

        OrderRequestDTO orderRequest = new OrderRequestDTO();
        orderRequest.setCustomerName("Bolatito");
        OrderItemDetailDTO orderLineItemRequest = new OrderItemDetailDTO();
        orderLineItemRequest.setProductId(1L);
        orderLineItemRequest.setQuantity(13);
        orderRequest.setItems(Collections.singletonList(orderLineItemRequest));


        // test the method
        assertThrows(IllegalArgumentException.class,() -> {
            orderService.placeOrder(orderRequest);
        }, "Product with id " + product.getId() + " is not available in sufficient quantity.");
    }

    @Test
    public void testOrderCreatedEventSent() {

        Product product = new Product(1L, "Hulu Boxer","Boxers for men",
                3,1500.0,1000.0, LocalDate.now());
        when(productService.getProductById(1L)).thenReturn(product);

        OrderRequestDTO orderRequest = new OrderRequestDTO();
        orderRequest.setCustomerName("Bolatito");
        OrderItemDetailDTO orderLineItemRequest = new OrderItemDetailDTO();
        orderLineItemRequest.setProductId(1L);
        orderLineItemRequest.setQuantity(3);
        orderRequest.setItems(Collections.singletonList(orderLineItemRequest));

        // create expected order and order line item objects
        Order expectedOrder = new Order();
        expectedOrder.setCustomerName("Bolatito");
        OrderItemDetail expectedLineItem = new OrderItemDetail();
        expectedLineItem.setProduct(product);
        expectedLineItem.setQuantity(3);
        expectedOrder.setOrderItemDetails(Collections.singletonList(expectedLineItem));

        // mock repository method
        when(orderRepository.save(any(Order.class))).thenReturn(expectedOrder);

        // test the method
        Order order = orderService.placeOrder(orderRequest);

        Consumer<String, Order> consumer = createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, TOPIC);
        ConsumerRecord<String, Order> consumerRecord = KafkaTestUtils.getSingleRecord(consumer, TOPIC);
        assertThat(consumerRecord.key()).isEqualTo("OrderCreatedEvent");
        assertThat(consumerRecord.value()).isEqualTo(order);
        consumer.close();
    }

    private Consumer<String, Order> createConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("kafka-group-id-gp", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(TRUSTED_PACKAGES, "com.example.orderservice");
        DefaultKafkaConsumerFactory<String, Order> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);
        return consumerFactory.createConsumer();
    }
}