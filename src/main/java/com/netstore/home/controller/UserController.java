package com.netstore.home.controller;

import com.netstore.home.model.User;
import com.netstore.home.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/newUser")
    public String addUser() {
        return "auth/newUser";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@ModelAttribute("newUser") final User newUser) {
        if (userService.saveUser(newUser)) {
            return "redirect:/user/success";
        }
        return "redirect:/user/error";
    }


    @GetMapping("/user/success")
    public String getSuccessPage() {
        return "auth/success";
    }

    @GetMapping("/user/error")
    public String getErrorPage() {
        return "auth/error";
    }

}
