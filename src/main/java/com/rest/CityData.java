package com.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CityData {

    @JsonProperty("city_name")
    private String cityName;
    @JsonProperty("country_code")
    private String countryCode;
    private String lat;
    private String lon;
    private WeatherData[] data;
}
