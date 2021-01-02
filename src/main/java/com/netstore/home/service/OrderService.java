package com.netstore.home.service;

import com.netstore.home.model.Order;
import com.netstore.home.model.OrderStatus;
import com.netstore.home.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Order save(Order order) {
        log.info("IN save OrderService");
        return orderRepository.save(order);
    }

    public Page<Order> findByUserName(String userName, Pageable pageable) {
        return orderRepository.findByUserName(userName, pageable);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public void setOrderFinished(Long id) {
        Order order = orderRepository.getOne(id);
        order.setOrderStatus(OrderStatus.FINISHED);
        orderRepository.save(order);
    }
}
