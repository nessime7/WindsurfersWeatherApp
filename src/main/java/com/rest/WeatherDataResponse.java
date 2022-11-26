package com.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeatherDataResponse {

    @JsonProperty("cityName")
    private String city_name;
    @JsonProperty("countryCode")
    private String country_code;
    @JsonProperty("windSpd")
    private double wind_spd;
    private double temp;

    public WeatherDataResponse(CityData cityRest, int specificDay) {
        // obiekt weather niepotrzebny
        // uważam że potrzebny :D - S
        WeatherData weather = cityRest.getData()[specificDay];
        city_name = cityRest.getCity_name();
        country_code = cityRest.getCountry_code();
        wind_spd = weather.getWind_spd();
        temp = weather.getTemp();
    }
}
