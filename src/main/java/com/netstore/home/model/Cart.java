package com.netstore.home.model;

import com.netstore.home.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@SessionScope
public class Cart {

    private final ProductService productService;

    private List<LineOrder> linesForOrder = new ArrayList<>();

    public Cart(ProductService productService) {
        log.info("created instance of Cart");
        this.productService = productService;
    }

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

    @PreDestroy
    public void destroyCart() {
        log.info("in destroy cart");
        if (!this.getLinesForOrder().isEmpty()) {

            for (LineOrder lineOrder : this.getLinesForOrder()) {

                Product product = productService.findById(lineOrder.getProduct().getId()).get();
                product.setQuantity(product.getQuantity() + lineOrder.getQuantity());
                productService.save(product);
            }
        }

    }

    public List<LineOrder> getLinesForOrder() {
        return linesForOrder;
    }

    public void setLinesForOrder(List<LineOrder> linesForOrder) {
        this.linesForOrder = linesForOrder;
    }
}
