package com.rest;

import lombok.Data;

@Data
public class WeatherDataResponse {

    // nie spojna konwecja nazewnictwa pol obiektow, camelCase vs snakeCase, powinna byc jedna konwencja w calym projekcie
    private String city_name;
    private String country_code;
    private double wind_spd;
    private double temp;

    public WeatherDataResponse(CityData cityRest, int specificDay) {
        // obiekt weather niepotrzebny
        WeatherData weather = cityRest.getData()[specificDay];
        city_name = cityRest.getCity_name();
        country_code = cityRest.getCountry_code();
        wind_spd = weather.getWind_spd();
        temp = weather.getTemp();
    }
}
