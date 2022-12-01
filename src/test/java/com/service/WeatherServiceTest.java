package com.service;

import com.config.WeatherBitApiConnector;
import com.model.City;
import com.repository.CityRepository;
import com.rest.CityData;
import com.rest.WeatherData;
import com.rest.WeatherDataResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherServiceTest {

    @Autowired
    private WeatherBitApiConnector restTemplateConfig;

    private final CityRepository cityRepository = mock(CityRepository.class);
    private final WeatherService weatherService = new WeatherService(restTemplateConfig, cityRepository);

    @Test
    void should_use_location_calculator_and_turn_70() {
        // given
        final var city = new WeatherDataResponse("Kraków", 20.0, 10.0);
        given(cityRepository.save(any())).willReturn(city);

        // when
        final var result = weatherService.bestLocationCalculator(city.getWindSpeed(), city.getTemperature());

        // then
        assertEquals(70.0, result);
    }

//    @Test
//    void should_return_city_with_better_weather_Warszawa() {
//        // given
//        final var firstCity = new WeatherDataResponse("Kraków",20.0,10.0);
//        final var secondCity = new WeatherDataResponse("Warszawa",21.0,11.0);
//        given(cityRepository.save(any())).willReturn(firstCity,secondCity);
//        given(cityRepository.findAll()).willReturn()
//
//        // when
//       final var weathers =
//        final var result = weatherService.getBestLocation(LocalDate.from(LocalDate.now()));
//
//
//        // then
//        assertEquals("Warszawa", result.getCityName());
//    }
}
