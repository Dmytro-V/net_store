package com.netstore.home.service;

import com.netstore.home.model.Product;
import com.netstore.home.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProductServiceIntegrationTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void whenFindById_thenReturnProduct() {
        //given
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        //when
        Product found = productService.findById(1L).get();

        //then
        assertThat(found.getId())
                .isEqualTo(1L);
    }

    @Test
    void whenFindAll_thenReturnAllProducts() {
        //given
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        Pageable pageable = Pageable.unpaged();
        Page<Product> products = new PageImpl<Product>(Arrays.asList(product1, product2, product3));

        when(productRepository.findAll(pageable))
                .thenReturn(products);

        //when
        Page<Product> foundProducts = productService.findAll(pageable);

        //then
        assertThat(foundProducts.getTotalElements()).isEqualTo(3L);
    }

    @Test
    void whenSave_thenInvokeSave() {
        //given
        Product product = new Product();
        when(productRepository.save(product)).thenReturn(product);

        //when
        Product createProduct = productService.save(product);

        //then
        verify(productRepository).save(product);
    }

    @Test
    void whenDeleteById_thenInvokeDelete() {
        //given
        doNothing().when(productRepository).deleteById(1L);

        //when
        productService.deleteById(1L);

        //then
        verify(productRepository).deleteById(1L);
    }

    @Test
    void whenFindMostQuantity_thenReturnThreeMostQuantity() {
        //given
        Product product1 = new Product();
        product1.setQuantity(10);
        Product product2 = new Product();
        product2.setQuantity(20);
        Product product3 = new Product();
        product3.setQuantity(30);
        Product product4 = new Product();
        product4.setQuantity(40);
        Pageable pageable = Pageable.unpaged();
        List<Product> products = Arrays.asList(product1, product2, product3, product4);

        when(productRepository.findAll(any(Sort.class)))
                .thenReturn(products);

        //when
        List<Product> foundProducts = productService.findMostQuantity();

        //then
        assertThat(foundProducts.size()).isEqualTo(3L);

    }
}
