package com.netstore.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

}
