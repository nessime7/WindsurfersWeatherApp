package com.service;

import com.rest.CityData;
import com.rest.WeatherData;
import com.rest.WeatherDataResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WeatherServiceTest {
    // zmienic nazwe cityRest
    // brak modyfikatorow dostepu
    // modele nie powinny byc tworzone jako pola obiektu
    // nie ma kontroli wersji
    CityData cityRest = new CityData();
    WeatherData weatherData = new WeatherData();
    WeatherData[] data = new WeatherData[1];
  //  WeatherService weatherService = new WeatherService();

    @Test
    void should_add_new_city_and_check_conditions() {
        // given
        // nie uzywac setterow do tworzenia testowych danych
        cityRest.setCity_name("Kraków");
        cityRest.setCountry_code("PL");
        weatherData.setTemp(20.0);
        weatherData.setWind_spd(10.0);
        data[0] = weatherData;
        cityRest.setData(data);

        // when
        // uzywac varow
        WeatherDataResponse weatherDataResponse = new WeatherDataResponse(cityRest, 0);

        // then
        // brakujacy static import
        Assertions.assertEquals("Kraków",weatherDataResponse.getCity_name());
        Assertions.assertEquals("PL",weatherDataResponse.getCountry_code());
        Assertions.assertEquals(20.0,weatherDataResponse.getTemp());
        Assertions.assertEquals(10.0,weatherDataResponse.getWind_spd());
     //   Assertions.assertEquals(10.0 * 3 + 20.0, weatherService.bestLocationCalculator(10.0,20.0));
    }
}
