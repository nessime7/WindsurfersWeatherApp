package com.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDataResponse {

    private String cityName;
    private double windSpeed;
    private double temperature;
}
