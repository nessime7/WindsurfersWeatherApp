package com.config;

import com.model.City;
import com.rest.CityData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class WeatherBitApiConnector {

    private final RestTemplate restTemplate;

    @Value("${weatherbit-api}")
    private String hostLink;

    @Autowired
    public WeatherBitApiConnector(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CityData getWeather(City city) {
        String key = "a29a7c73003045c28d3a2cc9592f1605";

        final var builder = UriComponentsBuilder.fromUriString(hostLink)
                .queryParam("city", city.getCityName())
                .queryParam("key", key)
                .build()
                .toUri();
        return restTemplate.getForObject(builder, CityData.class);
    }
}
