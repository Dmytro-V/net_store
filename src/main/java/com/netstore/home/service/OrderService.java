package com.netstore.home.service;

import com.netstore.home.model.Order;
import com.netstore.home.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order save(Order order) {
        log.info("IN save OrderService");
        return orderRepository.save(order);
    }

    public List<Order> findByUserName(String userName) {
        return orderRepository.findByUserName(userName);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
}
