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
    public void whenFindByTitle_thenReturnProducts() {
        //given
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        product1.setTitle("Test product1");
        product2.setTitle("Test product2");
        product3.setTitle("product3");

        entityManager.persist(product1);
        entityManager.persist(product2);
        entityManager.persist(product3);
        entityManager.flush();
        Pageable pageable = Pageable.unpaged();

        //when
        Page<Product> found = productRepository.findByTitleContaining("Test", pageable);

        //then
        assertThat(found.getTotalElements()).isEqualTo(2L);
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
