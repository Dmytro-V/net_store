package com.netstore.home.controller;

import com.netstore.home.model.Product;
import com.netstore.home.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
public class MainController {

    private final  ProductService productService;

    public MainController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping({"/", "/index"})
    public String getHome(Model model) {

        List<Product> mostQuantityProduct = productService.findMostQuantity();
        log.info("list  of mostQuantityProduct size= " + mostQuantityProduct.size());
        model.addAttribute("products", mostQuantityProduct);

        return "index";
    }

    @GetMapping("/about")
    public String getAbout() {
        return "about";
    }

    @GetMapping("/auth/login")
    public String getLoginPage() {
        return "auth/login";
    }

    @GetMapping("/auth/login-error")
    public String getLoginErrorPage(Model model) {
        model.addAttribute("loginError", true);
        return "auth/login";
    }


}
