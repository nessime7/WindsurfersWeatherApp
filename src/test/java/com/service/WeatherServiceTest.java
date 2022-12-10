package com.service;

import com.config.WeatherBitApiConnector;
import com.model.City;
import com.repository.CityRepository;
import com.rest.CityData;
import com.rest.WeatherData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// nie mockuję wywołań prywatnej klas w tej samej klasie

public class WeatherServiceTest {

    private final WeatherBitApiConnector weatherBitApiConnector = mock(WeatherBitApiConnector.class);
    private final CityRepository cityRepository = mock(CityRepository.class);
    private final WeatherService weatherService = new WeatherService(weatherBitApiConnector, cityRepository);

    @Test
    void should_return_better_location_Warsaw() {
        // given
        final var firstCityName = new City(UUID.randomUUID(), "Kraków");
        final var secondCityName = new City(UUID.randomUUID(), "Warszawa");
        when(cityRepository.findAll()).thenReturn(java.util.List.of(firstCityName, secondCityName));
        final var firstData = new WeatherData(20.0, 10.0, LocalDate.from(LocalDate.now()));
        final var secondData = new WeatherData(21.0, 11.0, LocalDate.from(LocalDate.now()));
        final var firstCity = new CityData("Kraków", "1", "1", Set.of(firstData));
        final var secondCity = new CityData("Warszawa", "1", "1", Set.of(secondData));
        when(weatherBitApiConnector.getWeather(firstCityName)).thenReturn(firstCity);
        when(weatherBitApiConnector.getWeather(secondCityName)).thenReturn(secondCity);

        // when
        final var result = weatherService.getBestLocation(LocalDate.from(LocalDate.now()));

        // then
        assertEquals("Warszawa", result.getCityName());
    }

    @Test
    void should_return_exception_null_data_response() {
        // given
        final var firstCityName = new City(UUID.randomUUID(), "Kraków");
        final var secondCityName = new City(UUID.randomUUID(), "Warszawa");
        when(cityRepository.findAll()).thenReturn(java.util.List.of(firstCityName, secondCityName));
        final var firstData = new WeatherData(2.0, 1.0, LocalDate.from(LocalDate.now()));
        final var secondData = new WeatherData(2.0, 1.0, LocalDate.from(LocalDate.now()));
        final var firstCity = new CityData("Kraków", "1", "1", Set.of(firstData));
        final var secondCity = new CityData("Warszawa", "1", "1", Set.of(secondData));
        when(weatherBitApiConnector.getWeather(firstCityName)).thenReturn(firstCity);
        when(weatherBitApiConnector.getWeather(secondCityName)).thenReturn(secondCity);

        // then
        var ex = assertThrows(IllegalArgumentException.class, () -> weatherService.getBestLocation(LocalDate.from(LocalDate.now())));
        assertThat(ex.getMessage(), is("No matching city found."));
    }

    @Test
    void should_return_exception_wrong_date() {
        // given
        final var firstCityName = new City(UUID.randomUUID(), "Kraków");
        final var secondCityName = new City(UUID.randomUUID(), "Warszawa");
        when(cityRepository.findAll()).thenReturn(java.util.List.of(firstCityName, secondCityName));
        final var firstData = new WeatherData(20.0, 10.0, LocalDate.from(LocalDate.now()));
        final var secondData = new WeatherData(21.0, 11.0, LocalDate.from(LocalDate.now()));
        final var firstCity = new CityData("Kraków", "1", "1", Set.of(firstData));
        final var secondCity = new CityData("Warszawa", "1", "1", Set.of(secondData));
        when(weatherBitApiConnector.getWeather(firstCityName)).thenReturn(firstCity);
        when(weatherBitApiConnector.getWeather(secondCityName)).thenReturn(secondCity);

        // then
        var ex = assertThrows(IllegalArgumentException.class, () -> weatherService.getBestLocation(LocalDate.of(2021, 12, 2)));
        assertThat(ex.getMessage(), is("Wrong date. You can choose 16 day range from today."));
    }
}
