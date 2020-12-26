package com.netstore.home.model;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
@SessionScope
public class Cart {

    private List<LineOrder> linesForOrder = new ArrayList<>();

    public void addLineOrder(LineOrder lineOrder) {
        this.linesForOrder.add(lineOrder);
    }
}
