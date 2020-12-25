package com.netstore.home.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @GetMapping({"/", "/index"})
    public String getHome() {
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

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cart")
    public String getCart() {
        return "cart";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cart/addProduct/{id}")
    public String addProductToCart(@PathVariable("id") Long id, @RequestParam ("add-quantity") int quantity) {
        System.out.println("id: " + id + " quantity: " + quantity);

        return "cart";
    }

}
