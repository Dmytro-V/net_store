package com.netstore.home.model;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
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

    public BigDecimal getTotalCost() {

        BigDecimal sum = BigDecimal.ZERO;
        for (LineOrder lineOrder : linesForOrder) {
            int quantity = lineOrder.getQuantity();
            BigDecimal price = lineOrder.getProduct().getPrice();
            sum = sum.add(price.multiply(new BigDecimal(quantity)));
        }

        return sum;
    }
}
