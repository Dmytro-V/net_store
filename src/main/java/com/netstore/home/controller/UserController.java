package com.netstore.home.controller;

import com.netstore.home.model.Order;
import com.netstore.home.model.User;
import com.netstore.home.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Slf4j
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

        if (userService.addUser(newUser)) {
            return "redirect:/addUser/success";
        }
        return "redirect:/addUser/error";
    }

    @GetMapping("/addUser/success")
    public String getSuccessPage() {
        return "auth/success";
    }

    @GetMapping("/addUser/error")
    public String addUserError(Model model) {
        model.addAttribute("addUserError", true);
        return "auth/newUser";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = {"/getAllUsers"})
    public String getUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "auth/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = {"/getUser/{id}"})
    public String getOrder(@PathVariable("id") Long id, Model model) {
        log.info("in getUser id: " + id);
        User user = userService.findById(id).get();
        model.addAttribute("user", user);
        return "auth/user";
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping(value = {"/getProfile"})
    public String getOrder(Model model, Principal principal) {
        log.info("in getProfile");
        User user = userService.findByName(principal.getName()).get();
        model.addAttribute("user", user);
        return "auth/profile";

    }

}
