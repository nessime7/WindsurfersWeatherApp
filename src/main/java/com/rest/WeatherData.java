package com.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class WeatherData {

    @JsonProperty("temp")
    private double temperature;
    @JsonProperty("wind_spd")
    private double windSpeed;
}
