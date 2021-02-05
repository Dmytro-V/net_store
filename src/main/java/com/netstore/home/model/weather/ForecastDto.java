package com.netstore.home.model.weather;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@Data
@SessionScope
public class ForecastDto {

    private String city;
    private int temp;
}
