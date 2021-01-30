package com.netstore.home.service;


import com.netstore.home.model.LineOrder;
import com.netstore.home.repository.LineOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineOrderService {

    private final LineOrderRepository lineOrderRepository;

    public LineOrderService(LineOrderRepository lineOrderRepository) {
        this.lineOrderRepository = lineOrderRepository;
    }

    public List<LineOrder> findAll() {
        return lineOrderRepository.findAll();
    }

    public LineOrder save(LineOrder lineOrder) {
        return lineOrderRepository.save(lineOrder);
    }
}
