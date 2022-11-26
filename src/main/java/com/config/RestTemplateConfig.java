package com.config;

import com.model.City;
import com.rest.CityData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    public RestTemplate restTemplate;

    @Autowired
    public RestTemplateConfig(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // https://api.weatherbit.io/v2.0/forecast/daily?city=Raleigh,NC&key=API_KEY
    public CityData getWeather(City city) {
        String url = "https://api.weatherbit.io/v2.0/forecast/daily";
        String apiKey = "c84ad94814fb4457b070f396b4029306";
        String appUrl = url + "?city=" + city.getCityName() + "country=" + city.getCountryCode() + "&key=" + apiKey;
        return restTemplate.getForObject(appUrl, CityData.class);
    }
}
