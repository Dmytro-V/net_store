package com.netstore.home.controller;

import com.netstore.home.model.Cart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MainController {

    private final Cart cart;

    public MainController(Cart cart) {
        this.cart = cart;
    }


    @GetMapping({"/", "/index"})
    public String getHome() {

        return "redirect:/products";
    }

    @GetMapping("/about")
    public String getAbout(Model model) {
        if (!cart.getLinesForOrder().isEmpty()) {
            model.addAttribute("cart", cart.getLinesForOrder().size());
        }
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
