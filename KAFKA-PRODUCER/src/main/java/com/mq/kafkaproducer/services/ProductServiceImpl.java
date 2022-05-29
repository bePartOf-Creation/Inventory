package com.mq.kafkaproducer.services;

import com.mq.kafkaproducer.builder.EntityBuilder;
import com.mq.kafkaproducer.constants.OrderConstant;
import com.mq.kafkaproducer.constants.PaymentMethod;
import com.mq.kafkaproducer.dtos.request.OrderDetailDTO;
import com.mq.kafkaproducer.dtos.request.ProductDTO;
import com.mq.kafkaproducer.models.Customer;
import com.mq.kafkaproducer.models.Order;
import com.mq.kafkaproducer.models.OrderDetails;
import com.mq.kafkaproducer.models.Product;
import com.mq.kafkaproducer.repository.CustomerRepository;
import com.mq.kafkaproducer.repository.OrderRepository;
import com.mq.kafkaproducer.repository.ProductRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * The type Product service.
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    ModelMapper modelMapper;


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
        Product foundProduct = this.productRepository.findById(id).orElse(null);
        log.info("::::  foundProduct id is --->{}", foundProduct.getId());
        if (foundProduct == null) {
            throw new IllegalArgumentException("Product Not Found");
        }
        this.modelMapper.map(productDTO, foundProduct);
        return this.productRepository.save(foundProduct);
    }

    @Override
    public Page<Product> listAllProducts(int pageNumber, int pageSize) {

        log.info(":::PageSize --> {}", pageSize);
        log.info(":::PageNumber --> {}", pageNumber);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "productCreatedDate");

        return this.productRepository.findAll(pageable);
    }

    @Override
    public Order placeOrder(Long customerId, Long productId, OrderDetailDTO orderDetailDTO) {
        Product orderProduct = this.productRepository.findById(productId).orElse(null);
        Customer orderingCustomer = this.customerRepo.findById(customerId).orElse(null);
        
        if (orderProduct == null && orderingCustomer == null) {
          throw new IllegalArgumentException(orderingCustomer.getFirstName() + "this" + orderProduct.getName() + "is not available");
        }
        
        OrderDetails orderDetails = getOrderDetails(orderProduct, orderDetailDTO);

        // Build the Customer Address.
        final String orderCustomerAddress = orderingCustomer.getCustomerAddress().getAddressNumber() + " " +
                orderingCustomer.getCustomerAddress().getNameOfStreet() + " " + orderingCustomer.getCustomerAddress().getStateOfCity();
       
       //Place Order.
        Order newOrder = Order.builder()
                .orderPrice(orderProduct.getProductPrice()).orderDate(LocalDate.now()).productCost(orderProduct.getProductCost())
                .customerAddress(orderCustomerAddress).orderingCustomer(orderingCustomer).paymentMethod(PaymentMethod.DEBIT_CARD)
                .phoneNumber(orderingCustomer.getPhoneNumber()).shippingCost(orderDetails.getShippingCost()).tax(OrderConstant.TAX).subTotal(orderDetails.getSubTotal()).
                build();
        newOrder.getOrderDetails().add(orderDetails);
        newOrder.setTotal(calculateTotalOrder(newOrder.getSubTotal(), newOrder.getTax()));

        return this.orderRepo.save(newOrder);
    }

    private Double calculateTotalOrder(Double subTotal, Double tax) {
        return subTotal + tax;
    }

    private OrderDetails getOrderDetails(Product orderProduct, OrderDetailDTO orderDetailDTO) {
        orderDetailDTO.setProductCost(orderProduct.getProductCost());
        orderDetailDTO.setQuantity(orderProduct.getQuantity());
        orderDetailDTO.setUnitPrice(calculateUnitPrice(orderProduct.getProductCost(), orderProduct.getQuantity()));
        orderDetailDTO.setShippingCost(OrderConstant.SHIPPING_COST);
        orderDetailDTO.setSubTotal(calculateSubTotal(orderDetailDTO.getUnitPrice(), orderDetailDTO.getShippingCost()));

        return OrderDetails.builder()
                .product(orderProduct)
                .productCost(orderDetailDTO.getProductCost())
                .shippingCost(orderDetailDTO.getShippingCost())
                .quantity(orderDetailDTO.getQuantity())
                .unitPrice(orderDetailDTO.getUnitPrice())
                .subTotal(orderDetailDTO.getSubTotal())
                .build();
    }

    private Double calculateUnitPrice(Double productCost, Integer quantity) {
        return productCost * quantity;
    }

    private Double calculateSubTotal(Double unitPrice, Double shippingCost) {
        return unitPrice * shippingCost;
    }

    /**
     * Check if product exists string.
     *
     * @param productDTO the product dto
     * @return the string
     */
    public String checkIfProductExists(ProductDTO productDTO) {
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
