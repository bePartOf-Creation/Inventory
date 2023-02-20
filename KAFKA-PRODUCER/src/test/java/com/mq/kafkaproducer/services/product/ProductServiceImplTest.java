package com.mq.kafkaproducer.services.product;

import com.mq.kafkaproducer.controllers.ProductController;
import com.mq.kafkaproducer.dtos.request.ProductDTO;
import com.mq.kafkaproducer.models.Product;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
class ProductServiceImplTest {

    @Mock
    private final ProductServiceImpl productService;

    @InjectMocks
    private ProductController productController;

    @Test
    public void testCreateProduct() {
        // arrange
        ProductDTO request = new ProductDTO();
        request.setName("Hulu Boxers");
        request.setShortDescription("Boxers For Men");
        request.setQuantity(10);
        request.setProductCost(1000.0);
        request.setProductPrice(1500.0);

        Product product = new Product();
        product.setId(1L);
        product.setName(request.getName());
        product.setShortDescription(request.getShortDescription());
        product.setProductPrice(request.getProductPrice());
        product.setQuantity(request.getQuantity());

        when(productService.createANewProduct(request)).thenReturn(product);

        // act
        ResponseEntity<?> response = productController.createNewProduct(request);

        // assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(product, response.getBody());
    }


    @Test
    public void testUpdateProduct() {
        // arrange
        Long id = 1L;
        ProductDTO request = new ProductDTO();
        request.setName("Hulu Boxers");
        request.setShortDescription("Boxers For Men");
        request.setQuantity(10);
        request.setProductCost(1000.0);
        request.setProductPrice(1500.0);

        Product product = new Product();
        product.setId(id);
        product.setName(request.getName());
        product.setShortDescription(request.getShortDescription());
        product.setProductPrice(request.getProductPrice());
        product.setQuantity(request.getQuantity());

        when(productService.updateProduct(any(), eq(id))).thenReturn(product);

        // act
        ResponseEntity<?> response = productController.updateProduct(request,id);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
    }


    @Test
    public void testGetAllProducts() {
        // arrange
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Hulu Boxers");
        product1.setQuantity(10);
        product1.setProductCost(1000.0);
        product1.setProductPrice(1500.0);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Women Shades");
        product2.setQuantity(20);
        product2.setProductCost(9000.0);
        product2.setProductPrice(12000.0);

        products.add(product1);
        products.add(product2);

        Page<Product> productInPages = new PageImpl<>(products);

        when(productService.listAllProducts(0,2)).thenReturn(productInPages);

        // act
        ResponseEntity<?> response = productController.allProduct(0,2);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productInPages, response.getBody());
    }





}




