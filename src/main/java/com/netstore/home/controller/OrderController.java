package com.netstore.home.controller;

import com.netstore.home.model.*;
import com.netstore.home.service.LineOrderService;
import com.netstore.home.service.OrderService;
import com.netstore.home.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
public class OrderController {

    private final LineOrderService lineOrderService;
    private final ProductService productService;
    private final Cart cart;
    private final OrderService orderService;



    @Autowired
    public OrderController(LineOrderService lineOrderService, ProductService productService, Cart cart, OrderService orderService) {
        this.lineOrderService = lineOrderService;
        this.productService = productService;
        this.cart = cart;
        this.orderService = orderService;
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cart")
    public String getCart(Model model) {
        List<LineOrder> linesForOrder = cart.getLinesForOrder();
        model.addAttribute("linesForOrder", linesForOrder);

        return "orders/cart";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cart/addProduct/{id}")
    public String addProductToCart(@PathVariable("id") Long id, @RequestParam("add-quantity") int quantity) {
        Product product = productService.findById(id).get();

        LineOrder lineOrder = new LineOrder();
        lineOrder.setProduct(product);
        lineOrder.setQuantity(quantity);

        cart.addLineOrder(lineOrder);

        return "redirect:/cart";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/addOrder")
    public String addOrder(Principal principal) {
        List<LineOrder> linesForOrder = cart.getLinesForOrder();
        log.info("IN addOrder, lines for order: " + linesForOrder.size());

        Order order = new Order();

        //TODO: transactional
        for (LineOrder lineOrder : linesForOrder) {
            Product product = productService.findById(lineOrder.getProduct().getId()).get();
            log.info("find product witn id: " + product.getId());

            //TODO: add error message
            if (lineOrder.getQuantity() > product.getQuantity()) {
                log.warn("product's quantity is not enough");
                return "redirect:/cart";
            }
            product.setQuantity(product.getQuantity() - lineOrder.getQuantity());
            productService.save(product);
            lineOrder = lineOrderService.save(lineOrder);
            order.getLinesForOrder().add(lineOrder);

        }
        order.setUserName(principal.getName());
        order.setCreationDate(new Date());
        order.setOrderStatus(OrderStatus.CREATED);
        orderService.save(order);
        cart.getLinesForOrder().clear();

        return "redirect:/getUserOrders";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = {"/getUserOrders"})
    public String getUserOrders(Model model, Principal principal) {
        log.info("in getUserOrders");

        List<Order> orders = orderService.findByUserName(principal.getName());

        model.addAttribute("orders", orders);
        return "orders/userOrders";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = {"/getUserOrder/{id}"})
    public String getOrder(@PathVariable ("id") Long id, Model model, Principal principal) {
        log.info("in getUserOrder id: " + id);

        Order order = orderService.findById(id).get();

        if (principal.getName().equals(order.getUserName())) {
            model.addAttribute("order", order);
            return "orders/userOrder";
        }
        return "redirect:/getUserOrders";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = {"/getAllOrders"})
    public String getAllOrders(Model model) {
        log.info("in getAllOrders");

        List<Order> orders = orderService.findAll();

        model.addAttribute("orders", orders);
        return "orders/allOrders";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = {"/getOrder/{id}"})
    public String getOrder(@PathVariable ("id") Long id, Model model) {
        log.info("in getOrder id: " + id);

        Order order = orderService.findById(id).get();

        model.addAttribute("order", order);
        return "orders/order";
    }
}
