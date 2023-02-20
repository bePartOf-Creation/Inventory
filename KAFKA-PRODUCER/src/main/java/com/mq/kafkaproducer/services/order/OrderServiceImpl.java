package com.mq.kafkaproducer.services.order;

import com.mq.kafkaproducer.constants.OrderConstant;
import com.mq.kafkaproducer.constants.PaymentMethod;
import com.mq.kafkaproducer.dtos.request.OrderItemDetailDTO;
import com.mq.kafkaproducer.dtos.request.OrderRequestDTO;
import com.mq.kafkaproducer.dtos.request.ProductDTO;
import com.mq.kafkaproducer.models.Order;
import com.mq.kafkaproducer.models.OrderItemDetail;
import com.mq.kafkaproducer.models.Product;
import com.mq.kafkaproducer.repository.OrderItemDetailRepository;
import com.mq.kafkaproducer.repository.OrderRepository;
import com.mq.kafkaproducer.repository.ProductRepository;
import com.mq.kafkaproducer.services.product.ProductServiceImpl;
import com.mq.kafkaproducer.utils.OrderUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
//@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private OrderItemDetailRepository orderItemDetailRepo;


    @Override
    public Order placeOrder(OrderRequestDTO orderRequestDTO) {
        //Create a new Order.
        Order newOrder = new Order();
        newOrder.setCustomerName(orderRequestDTO.getCustomerName());
        newOrder.setPhoneNumber(orderRequestDTO.getPhoneNumber());

        // For each item in the order, check the availability of the product and
        // create a new OrderItem object if the product is available.
        List<OrderItemDetail> orderItemDetails = new ArrayList<>();
        for (OrderItemDetailDTO orderItem:  orderRequestDTO.getItems()) {
            Long productId = orderItem.getProductId();
            int quantity = orderItem.getQuantity();

            Optional<Product> optionalProduct = Optional.ofNullable(productService.getProductById(productId));

            if (optionalProduct.isEmpty()) {
                throw new IllegalArgumentException("Product with id " + productId + " not found.");
            }

            Product product = optionalProduct.get();
            if (product.getQuantity() < quantity) {
                throw new IllegalArgumentException("Product with id " + productId + " is not available in sufficient quantity.");
            }

            // Create and Persist orderItem generated.
            OrderItemDetail orderItemDetail = OrderUtils.getOrderDetails(product, orderItem);
            orderItemDetail.setOrders(newOrder);
            orderItemDetails.add(orderItemDetail);

           log.info("Size of Product -> {}", orderItemDetails.size());
            // Decrement the product's quantity
            product.setQuantity(product.getQuantity() - quantity);
            log.info("Product found again -> {}" , product.getId());
            this.productService.save(product);
        }

        // Save the Order object and its associated OrderLineItem objects to the database
        newOrder.setOrderItemDetails(orderItemDetails);
        newOrder.setPaymentMethod(PaymentMethod.DEBIT_CARD);
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setTax(OrderConstant.TAX);

        // Calculate the total price of the order and set it on the Order object
        double totalPrice = orderItemDetails.stream().mapToDouble(OrderItemDetail::getUnitPrice).sum();
        newOrder.setTotal(OrderUtils.calculateTotalOrder(totalPrice, newOrder.getTax()));
        this.orderRepo.save(newOrder);

        return newOrder;
    }
}
