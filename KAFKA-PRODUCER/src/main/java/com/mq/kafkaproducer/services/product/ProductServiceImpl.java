package com.mq.kafkaproducer.services.product;

import com.mq.kafkaproducer.builder.EntityBuilder;
import com.mq.kafkaproducer.constants.OrderConstant;
import com.mq.kafkaproducer.constants.PaymentMethod;
import com.mq.kafkaproducer.dtos.request.OrderItemDetailDTO;
import com.mq.kafkaproducer.dtos.request.OrderRequestDTO;
import com.mq.kafkaproducer.dtos.request.ProductDTO;
import com.mq.kafkaproducer.models.Customer;
import com.mq.kafkaproducer.models.Order;
import com.mq.kafkaproducer.models.OrderItemDetail;
import com.mq.kafkaproducer.models.Product;
import com.mq.kafkaproducer.repository.CustomerRepository;
import com.mq.kafkaproducer.repository.OrderItemDetailRepository;
import com.mq.kafkaproducer.repository.OrderRepository;
import com.mq.kafkaproducer.repository.ProductRepository;
import com.mq.kafkaproducer.utils.OrderUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * The type Product service.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OrderItemDetailRepository orderItemDetailRepo;
    private final ModelMapper modelMapper;

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);


    @Override
    public Product createANewProduct(ProductDTO productDTO) {
        try {
            String checkDuplicateProduct = checkIfProductExists(productDTO);
            log.info("::: No Duplicate Results -> {}", checkDuplicateProduct);
        } catch (DataIntegrityViolationException ex) {
            log.info("::: Duplicate Product Found -> {}", ex.getMessage());
        }
        Product newProductToBeCreated = EntityBuilder.productBuilder(productDTO);
        return this.productRepository.save(newProductToBeCreated);
    }

    @Override
    public Product updateProduct(ProductDTO productDTO, Long id) {
        Optional<Product> foundProduct = this.productRepository.findById(id);
        if (foundProduct.isEmpty()) {
            throw new IllegalArgumentException("Product Not Found");
        }
        log.info("::::  foundProduct id is --->{}", foundProduct.get().getId());

        // Store the old price for later use
        double oldPrice = foundProduct.get().getProductPrice();

        // Update the product's price
        foundProduct.get().setProductPrice(productDTO.getProductPrice());

        productRepository.save(foundProduct.get());
        this.modelMapper.map(productDTO, foundProduct.get());
        this.productRepository.save(foundProduct.get());


        // Update the OrderItemDetaill objects that reference the updated product
        List<OrderItemDetail> orderLineItems = orderItemDetailRepo.findOrderItemDetailsByProduct(foundProduct.get());
        for (OrderItemDetail orderLineItem : orderLineItems) {
            // Calculate the new price for the OrderLineItem
            double oldLineItemPrice = orderLineItem.getUnitPrice();
            double priceFactor = foundProduct.get().getProductPrice() / oldPrice;
            double newLineItemPrice = oldLineItemPrice * priceFactor;

            // Update the OrderItemDetail's price
            orderLineItem.setUnitPrice(newLineItemPrice);
            this.orderItemDetailRepo.save(orderLineItem);
        }
        return foundProduct.get();
    }

    @Override
    public Page<Product> listAllProducts(int pageNumber, int pageSize) {
        log.info(":::PageSize --> {}", pageSize);
        log.info(":::PageNumber --> {}", pageNumber);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "productCreatedDate");

        return this.productRepository.findAll(pageable);
    }

    @Override
    public Product getProductById(long id) {
        return this.productRepository.findById(id).orElse(null);
    }


    public Product save(Product product){
        return this.productRepository.saveAndFlush(product);
    }
//
//    @Override
//    public Order placeOrder(OrderRequestDTO orderRequestDTO) {
//        //Create a new Order.
//        Order newOrder = new Order();
//        newOrder.setCustomerName(orderRequestDTO.getCustomerName());
//        newOrder.setPhoneNumber(orderRequestDTO.getPhoneNumber());
//
//        // For each item in the order, check the availability of the product and
//        // create a new OrderItem object if the product is available.
//        Set<OrderItemDetail> orderItemDetails = new HashSet<>();
//        this.orderUtils.processForAvailabilityBeforePlacing(
//                orderRequestDTO,
//                productRepository,
//                newOrder,
//                orderItemDetails
//        );
//        // Save the Order object and its associated OrderLineItem objects to the database
//        newOrder.setOrderItemDetails(orderItemDetails);
//        newOrder.setPaymentMethod(PaymentMethod.DEBIT_CARD);
//        newOrder.setTax(OrderConstant.TAX);
//
//        // Calculate the total price of the order and set it on the Order object
//        double totalPrice = orderItemDetails.stream().mapToDouble(OrderItemDetail::getUnitPrice).sum();
//        newOrder.setTotal(OrderUtils.calculateTotalOrder(totalPrice, newOrder.getTax()));
//        this.orderRepo.save(newOrder);
//
//        return newOrder;
//
//    }




    /**
     * Check if product exists string.
     *
     * @param productDTO the product dto
     * @return the string
     */
    private String checkIfProductExists(ProductDTO productDTO) {
        Optional<Product> foundProduct = this.productRepository.findByName(productDTO.getName());
        if (foundProduct.isPresent()) {
            throw new DataIntegrityViolationException("Duplicate Product with the same Name Cannot be Created");
        }
        return "New Product Can Be Created";
    }

    public String checkIfProductEmpty(Optional<Product> foundProduct) {
        if (foundProduct.isEmpty()) {
            throw new DataIntegrityViolationException("Duplicate Product with the same Name Cannot be Created");
        }
        return "New Product Can Be Created";
    }
}
