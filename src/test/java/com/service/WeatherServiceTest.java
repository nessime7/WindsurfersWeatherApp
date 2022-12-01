package com.service;

import com.config.WeatherBitApiConnector;
import com.model.City;
import com.repository.CityRepository;
import com.rest.CityData;
import com.rest.WeatherData;
import com.rest.WeatherDataResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherServiceTest {

    @Autowired
    private WeatherBitApiConnector weatherBitApiConnector;

    private final CityRepository cityRepository = mock(CityRepository.class);
    private final WeatherService weatherService = new WeatherService(weatherBitApiConnector, cityRepository);

//    @Test
//    void should_use_location_calculator_and_turn_70() {
//        // given
//        final var city = new WeatherDataResponse("Kraków", 20.0, 10.0);
//        given(cityRepository.save(any())).willReturn(city);
//
//        // when
//        final var result = weatherService.bestLocationCalculator(city.getWindSpeed(), city.getTemperature());
//
//        // then
//        assertEquals(70.0, result);
//    }

//    @Test
//    void should_return_city_with_better_weather_Warszawa() {
//        // tylko do ćwiczenia
//        final var firstCityName = new City(UUID.randomUUID(), "Kraków");
//        final var secondCityName = new City(UUID.randomUUID(), "Warszawa");
//        when(cityRepository.findAll()).thenReturn(java.util.List.of(firstCityName, secondCityName));
//
//        // given
//        final var firstData = new WeatherData(20.0, 10.0, LocalDate.from(LocalDate.now()));
//        final var secondData = new WeatherData(21.0, 11.0, LocalDate.from(LocalDate.now()));
//        final var firstCity = new CityData("Kraków", "1", "1", Set.of(firstData));
//        final var secondCity = new CityData("Warszawa", "1", "1", Set.of(secondData));
//        var weathers = java.util.List.of(firstCity, secondCity);
//        // when
//        final var result = weatherService.resolveWeather(LocalDate.from(LocalDate.now()), weathers);
//
//        // then
//        assertEquals("Warszawa", result.getCityName());
//    }
}
