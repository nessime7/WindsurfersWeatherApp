package com.config;

import com.model.City;
import com.rest.CityData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class WeatherBitApiConnector {

    private final RestTemplate restTemplate;

    @Value("${weatherbit-api}")
    private String hostLink;

    @Autowired
    public WeatherBitApiConnector(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // https://api.weatherbit.io/v2.0/forecast/daily?city=Raleigh,NC&key=API_KEY
    public CityData getWeather(City city) {
        String url = hostLink + "/v2.0/forecast/daily";
        String apiKey = "c84ad94814fb4457b070f396b4029306";
        String appUrl = url + "?city=" + city.getCityName() + "&key=" + apiKey;

        // new
        CityData result = restTemplate.getForObject(appUrl, CityData.class);

        return result;
      //  return restTemplate.getForObject(appUrl, CityData.class);
    }
}
