package com.service;

import com.config.WeatherBitApiConnector;
import com.repository.CityRepository;
import com.rest.WeatherDataResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class WeatherServiceTest {

    private WeatherBitApiConnector restTemplateConfig;
    private final CityRepository cityRepository = mock(CityRepository.class);
    private final WeatherService weatherService = new WeatherService(restTemplateConfig, cityRepository);
    private WeatherDataResponse weatherDataResponse;

    @Test
    void should_add_new_city_and_check_conditions() {
        // given
        var city = new WeatherDataResponse("Kraków", "PL", 10.0, 20.0);

        // then
        assertEquals("Kraków", city.getCityName());
        assertEquals("PL", city.getCountryCode());
        assertEquals(20.0, city.getTemperature());
        assertEquals(10.0, city.getWindSpeed());
        assertEquals(10.0 * 3 + 20.0, weatherService.bestLocationCalculator(10.0, 20.0));
    }
}
