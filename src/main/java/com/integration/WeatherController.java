package com.integration;

import com.rest.WeatherDataResponse;
import com.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;

@RestController
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather/{localDate}")
    WeatherDataResponse getLocationWithBestWeather(@PathVariable(value = "localDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate localDate) {
        return weatherService.getBestLocation(localDate);
    }
}
