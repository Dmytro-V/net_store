package com.netstore.home.controller;

import com.netstore.home.model.Cart;
import com.netstore.home.model.User;
import com.netstore.home.model.weather.Forecast;
import com.netstore.home.model.weather.ForecastDto;
import com.netstore.home.service.UserService;
import com.netstore.home.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Slf4j
@Controller
public class MainController {

    private final Cart cart;
    private final ForecastDto forecastDto;
    private final UserService userService;
    private final WeatherService weatherService;

    public MainController(Cart cart, ForecastDto forecastDto, UserService userService, WeatherService weatherService) {
        this.cart = cart;
        this.forecastDto = forecastDto;
        this.userService = userService;
        this.weatherService = weatherService;
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

    @GetMapping({"/", "/index"})
    public String getHome() {

        return "redirect:/products";
    }

    @GetMapping("/about")
    public String getAbout(Model model) {
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

    @GetMapping("/weather")
    public String frag1(Principal principal, Model model) {
        if (principal != null) {
            Optional<User> user = userService.findByName(principal.getName());
            if (user.isPresent()) {
                try {
                    Forecast forecast = weatherService.getForecast(user.get().getCity());
                    model.addAttribute("content_name", forecast.getName());
                    model.addAttribute("content_temp", forecast.getMain().getTemp().intValue());
                } catch (IOException e) {
                    log.warn("can't find weather");
                }
            }
        }
        return "dynamic/top-bar::content_weather";
    }


}
