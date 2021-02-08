package com.netstore.home.repository;

import com.netstore.home.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
class ProductRepositoryIntegrationTest {
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void whenFindByTitle_thenReturnProduct() {
        //given
        Product testProduct = new Product();
        testProduct.setTitle("Test product");
        entityManager.persist(testProduct);
        entityManager.flush();

        //when
        Product found = productRepository.findByTitle("Test product").get();

        //then
        assertThat(found.getTitle()).isEqualTo(testProduct.getTitle());
    }

    @Test
    public void whenFindByAll_thenReturnAllProducts() {
        //given
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        entityManager.persist(product1);
        entityManager.persist(product2);
        entityManager.persist(product3);
        entityManager.flush();
        Pageable pageable = Pageable.unpaged();

        //when
        Page<Product> products =  productRepository.findAll(pageable);

        //then
        assertThat(products.getTotalElements()).isEqualTo(3L);
    }
    
}
