package com.mq.kafkaproducer.services;

import com.mq.kafkaproducer.dtos.request.ProductDTO;
import com.mq.kafkaproducer.models.Product;
import com.mq.kafkaproducer.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ProductServiceImplTest {
    
   
    @Autowired private ProductServiceImpl productService;
    @Autowired private ProductRepository productRepo;
    
    @BeforeEach
    public void setUp(){
//        Product product1 = Product.builder().name("Samsung Tv").productCost(50000).quantity(10).productPrice(57000).productCreatedDate(LocalDate.now()).shortDescription("LongLasting Home Tv").build();
//        Product product2 = Product.builder().name("Female Top").productCost(2000).quantity(50).productPrice(7000).productCreatedDate(LocalDate.now()).shortDescription("Top Designers").build();
//        Product product3 = Product.builder().name("Male Jeans").productCost(8700).quantity(10).productPrice(17000).productCreatedDate(LocalDate.now()).shortDescription("Rock Star Jeans").build();
//        Product product4 = Product.builder().name("Tea Spoons").productCost(500).quantity(5).productPrice(1000).productCreatedDate(LocalDate.now()).shortDescription("Durable Spoons").build();
//        Product product5 = Product.builder().name("Rechargeable Light").productCost(5000).quantity(40).productPrice(7000).productCreatedDate(LocalDate.now()).shortDescription("Shine oN").build();
//        Product product6 = Product.builder().name("Dildos").productCost(3000).quantity(40).productPrice(7000).productCreatedDate(LocalDate.now()).shortDescription("Sex Pleasures").build();
//        Product product7 = Product.builder().name("Chin Chin").productCost(50).quantity(100).productPrice(100).productCreatedDate(LocalDate.now()).shortDescription("Adunni Edibles").build();
//
//        this.productRepo.save(product1);
//        this.productRepo.save(product2);
//        this.productRepo.save(product3);
//        this.productRepo.save(product4);
//        this.productRepo.save(product5);
//        this.productRepo.save(product6);
//        this.productRepo.save(product7);
    }
    private final Logger log = LoggerFactory.getLogger(ProductServiceImplTest.class);
    
   

    @Test
    void createANewProduct() {
        ProductDTO product1 = ProductDTO.builder().name("Hulu Boxers").productCost(2400.0).quantity(10).productPrice(26000.0).shortDescription("Best For Men").build();
        Product newProductToBeCreated = this.productService.createANewProduct(product1);
        
        assertThat(newProductToBeCreated.getProductCreatedDate()).isEqualTo(LocalDate.now());
        assertThat(newProductToBeCreated.getName()).isEqualTo("Hulu Boxers");
        assertThat(newProductToBeCreated.getQuantity()).isEqualTo(10);
    }
    @Test
    void updateProduct() {
        ProductDTO productDTO = ProductDTO.builder().name("Tixi Boxers").productCost(2400.0).quantity(1).productPrice(26000.0).shortDescription("Best For Men").build();
        Product createdProduct = this.productService.createANewProduct(productDTO);
        
        ProductDTO updatedProductDTO = new ProductDTO();
        updatedProductDTO.setName("Yulu Boxers");
        
        Product updatedProduct = this.productService.updateProduct(updatedProductDTO, createdProduct.getId());
        assertThat(updatedProduct.getName()).isEqualTo("Yulu Boxers");
    }

    @Test
    void testThatTruckCanBeGroupedIntoPages() {
        Page<Product> pagedProduct = this.productService.listAllProducts(0, 5);
        Assertions.assertThat(pagedProduct.getContent()).size().isEqualTo(5);
    }
    @Test
    void placeOrder() {
    }
}
