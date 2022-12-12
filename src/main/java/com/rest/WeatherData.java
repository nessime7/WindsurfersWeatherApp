package com.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherData {

    @JsonProperty("temp")
    private double temperature;
    @JsonProperty("wind_spd")
    private double windSpeed;
    @JsonProperty("valid_date")
    private LocalDate validDate;
}
