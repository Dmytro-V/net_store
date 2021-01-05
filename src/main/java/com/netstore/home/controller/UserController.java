package com.netstore.home.controller;

import com.netstore.home.model.*;
import com.netstore.home.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class UserController {

    private final UserService userService;
    private final Cart cart;

    @Autowired
    public UserController(UserService userService, Cart cart) {
        this.userService = userService;
        this.cart = cart;
    }

    @ModelAttribute("cartSize")
    public int getCartSize() {
        return cart.getLinesForOrder().size();
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
    public String getUsers(Model model, @PageableDefault(size=5, sort="email") Pageable pageable) {
        Page<User> pageUsers = userService.findAll(pageable);
        model.addAttribute("page", pageUsers);

        List<Sort.Order> sortOrders = pageUsers.getSort().stream().collect(Collectors.toList());
        if (sortOrders.size() > 0) {
            Sort.Order order = sortOrders.get(0);
            model.addAttribute("sortProperty", order.getProperty());
            model.addAttribute("sortDesc", order.getDirection() == Sort.Direction.DESC);
        }

        return "auth/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = {"/getUser/{id}"})
    public String getOrder(@PathVariable("id") Long id, Model model) {
        log.info("in getUser id: " + id);
        User user = userService.findById(id).get();
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("statuses", Status.values());
        return "auth/profile";
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping(value = {"/getProfile"})
    public String getProfile(Model model, Principal principal) {
        log.info("in getProfile");
        User user = userService.findByName(principal.getName()).get();
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("statuses", Status.values());

        return "auth/profile";
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping(value = {"/updateUser"})
    public String updateUser(@ModelAttribute("user") final User updateUser, Principal principal) {
        log.info("in updateUser");
        User user = userService.findByName(updateUser.getEmail()).get();
        user.setCity(updateUser.getCity());
        user.setAddress(updateUser.getAddress());
        user.setMessage(updateUser.getMessage());
        user.setAvatar(updateUser.getAvatar());

        if (userService.findByName(principal.getName()).get().getRole().equals(Role.ADMIN)) {
            user.setRole(updateUser.getRole());
            user.setStatus(updateUser.getStatus());
            userService.save(user);
            return "redirect:/getAllUsers";
        }

        userService.save(user);
        return "redirect:/";
    }

}
