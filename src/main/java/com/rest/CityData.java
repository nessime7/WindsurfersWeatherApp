package com.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CityData {

    private String city_name;
    private String country_code;
    private String lat;
    private String lon;
    private WeatherData[] data;
}
