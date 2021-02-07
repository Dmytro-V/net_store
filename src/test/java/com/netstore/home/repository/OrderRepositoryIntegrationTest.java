package com.netstore.home.repository;

import com.netstore.home.model.Order;
import com.netstore.home.model.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class OrderRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    private Pageable pageable;

    @BeforeEach
    void setOrders() {
        //given
        Order order1 = new Order();
        order1.setUserName("user1");
        Order order2 = new Order();
        order2.setUserName("user1");
        Order order3 = new Order();
        order3.setUserName("user2");
        entityManager.persist(order1);
        entityManager.persist(order2);
        entityManager.persist(order3);
        entityManager.flush();

        this.pageable = Pageable.unpaged();

    }

    @Test
    void whenFindByUserName_thenReturnPageOrders() {

        //when
        Page<Order> foundOrders = orderRepository.findByUserName("user1", pageable);

        //then
        assertThat(foundOrders.getTotalElements()).isEqualTo(2L);
    }

    @Test
    void findAll() {

        //when
        Page<Order> orders =  orderRepository.findAll(pageable);

        //then
        assertThat(orders.getTotalElements()).isEqualTo(3L);
    }
}
