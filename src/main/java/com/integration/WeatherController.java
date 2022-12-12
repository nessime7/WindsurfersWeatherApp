package com.integration;

import com.rest.WeatherDataResponse;
import com.service.WeatherService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather/{localDate}")
    ResponseEntity<WeatherDataResponse> getLocationWithBestWeather(@PathVariable(value = "localDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate localDate) {
        return ResponseEntity.ok(weatherService.getBestLocation(localDate));
    }
}
