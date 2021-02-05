package com.netstore.home.service;

import com.google.gson.Gson;
import com.netstore.home.model.weather.Forecast;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Component
@SessionScope
@Slf4j
public class WeatherService {

    @Value("${url-weather}")
    private String urlWeather;

    public Forecast getForecast(String city) throws IOException {
        log.info("get weather");
        URL url = new URL(urlWeather + city);
        InputStreamReader reader = new InputStreamReader(url.openStream());
        return  new Gson().fromJson(reader, Forecast.class);

    }
}
