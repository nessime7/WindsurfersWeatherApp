package com.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CityData {

    @JsonProperty("city_name")
    private String cityName;
    private String lat;
    private String lon;
    private Set<WeatherData> data;
}
