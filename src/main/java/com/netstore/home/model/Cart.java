package com.netstore.home.model;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Cart {

    private List<LineOrder> linesForOrder;
}
