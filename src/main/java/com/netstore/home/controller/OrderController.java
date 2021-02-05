package com.netstore.home.controller;

import com.netstore.home.model.*;
import com.netstore.home.model.weather.ForecastDto;
import com.netstore.home.service.LineOrderService;
import com.netstore.home.service.OrderService;
import com.netstore.home.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class OrderController {

    private final LineOrderService lineOrderService;
    private final ProductService productService;
    private final Cart cart;
    private final OrderService orderService;
    private final ForecastDto forecastDto;

    public OrderController(LineOrderService lineOrderService, ProductService productService, Cart cart, OrderService orderService, ForecastDto forecastDto) {
        this.lineOrderService = lineOrderService;
        this.productService = productService;
        this.cart = cart;
        this.orderService = orderService;
        this.forecastDto = forecastDto;
    }

    @ModelAttribute("cartSize")
    public int getCartSize() {
        return cart.getLinesForOrder().size();
    }

    @ModelAttribute("forecastCity")
    public String getForecastCity(){
        return forecastDto.getCity();
    }

    @ModelAttribute("forecastTemp")
    public int getForecastTemp(){
        return forecastDto.getTemp();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cart")
    public String getCart(Model model) {
        List<LineOrder> linesForOrder = cart.getLinesForOrder();
        model.addAttribute("linesForOrder", linesForOrder);
        model.addAttribute("totalCostOfCart", cart.getTotalCost());
        return "orders/cart";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/deleteLine/{index}")
    @Transactional
    public String deleteLine(@PathVariable("index") int index) {
        List<LineOrder> linesForOrder = cart.getLinesForOrder();

        Product product = productService.findById(linesForOrder.get(index).getProduct().getId()).get();
        product.setQuantity(product.getQuantity() + linesForOrder.get(index).getQuantity());
        productService.save(product);

        linesForOrder.remove(index);
        return "redirect:/cart";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cart/addProduct/{id}")
    @Transactional
    public String addProductToCart(@PathVariable("id") Long id,
                                   @RequestParam("add-quantity") int quantity) throws Exception {
        Product product = productService.findById(id).get();

        if (quantity > product.getQuantity()) {
            throw new Exception("product's quantity is not enough");
        }

        LineOrder lineOrder = new LineOrder();
        lineOrder.setProduct(product);
        lineOrder.setQuantity(quantity);

        product.setQuantity(product.getQuantity() - lineOrder.getQuantity());
        productService.save(product);

        cart.addLineOrder(lineOrder);

        return "redirect:/cart";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/addOrder")
    @Transactional
    public String addOrder(Principal principal) {
        List<LineOrder> linesForOrder = cart.getLinesForOrder();
        log.info("IN addOrder, lines for order: " + linesForOrder.size());

        Order order = new Order();

        for (LineOrder lineOrder : linesForOrder) {
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
    public String getUserOrders(Model model, Principal principal, @PageableDefault(size = 5, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("in getUserOrders");

        Page<Order> orders = orderService.findByUserName(principal.getName(), pageable);
        model.addAttribute("page", orders);

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
    public String getAllOrders(Model model, @PageableDefault(size=5, sort="creationDate", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("in getAllOrders");

        Page<Order> pageOrders = orderService.findAll(pageable);
        model.addAttribute("page", pageOrders);

        List<Sort.Order> sortOrders = pageOrders.getSort().stream().collect(Collectors.toList());
        if (sortOrders.size() > 0) {
            Sort.Order sortOrder = sortOrders.get(0);
            model.addAttribute("sortProperty", sortOrder.getProperty());
            model.addAttribute("sortDesc", sortOrder.getDirection() == Sort.Direction.DESC);
        }

        return "orders/allOrders";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = {"/getOrder/{id}"})
    public String getOrder(@PathVariable ("id") Long id, Model model) {
        log.info("in getOrder id: " + id);

        Order order = orderService.findById(id).get();

        model.addAttribute("order", order);
        model.addAttribute("orderFinished", OrderStatus.FINISHED);

        return "orders/order";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = {"/order/{id}/setFinished"})
    public String setOrderFinished(@PathVariable("id") Long id) {
        log.info("in setOrderFinished id: " + id);

        orderService.setOrderFinished(id);

        return "redirect:/getOrder/" + id;
    }
}
