package com.service;

import com.config.MenuManagerExceptionMessages;
import com.model.City;
import com.repository.CityRepository;
import com.rest.CityData;
import com.rest.WeatherDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WeatherService {
    private final RestTemplate restTemplate;
    private final CityRepository cityRepository;

    @Autowired
    public WeatherService(RestTemplate restTemplate, CityRepository cityRepository) {
        this.restTemplate = restTemplate;
        this.cityRepository = cityRepository;
    }

    public WeatherDataResponse getBestLocation(Date date) {
        List<WeatherDataResponse> result = getWeatherForAllCities(checkDate(date));
        WeatherDataResponse weatherResult = null;
        final var bestLocationValue = 0;
        for (WeatherDataResponse item : result) {
            if ((item.getTemp() > 5 && item.getTemp() < 35) &&
                    (item.getWind_spd() > 5 && item.getWind_spd() < 18)) {
                final var currentCityLocationValue = bestLocationCalculator(item.getWind_spd(), item.getTemp());
                if (currentCityLocationValue > bestLocationValue) {
                    weatherResult = item;
                }
            }
        }
        return weatherResult;
//        return result.stream()
//                .filter(city -> city.getTemp() > 5 && city.getTemp() < 35)
//                .filter(city -> city.getWind_spd() > 5 && city.getWind_spd() < 18)
//                .max(Comparator.comparing(weather -> bestLocationCalculator(weather.getWind_spd(), weather.getTemp())))
//                .orElse(null);
    }

    private List<WeatherDataResponse> getWeatherForAllCities(int specificDay) {
        // brak final varow
        List<WeatherDataResponse> response = new ArrayList<>();
        List<City> cities = cityRepository.findAll();
        for (City city : cities) {
            // prosze zmienic nazwe cityRest na cos bardziej meaningful
            CityData cityData = getWeather(city);
            WeatherDataResponse weatherDataResponse = new WeatherDataResponse(cityData, specificDay);
            response.add(weatherDataResponse);
        }
        return response;
    }

    // prosze nie uzywac klasy date, prosze uzyc LocalDate, prosze zmienic nazwe na cos bardziej meaningful,
    // moze byc daysToRequestedDate
    private int checkDate(Date date) {
        // nie powinno sie pozostawiac komentarzy, brak final varow
        // przekonwertowanie z java time LocalDate na java util Date
        // przekształcenie daty w ciąg znaków
        Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        long differenceDays = ChronoUnit.DAYS.between(currentDate.toInstant(), date.toInstant());
        if (differenceDays >= 0 && differenceDays < 16) {
            // czy można bez rzutowania?   int 1-32300210312, 1-3230021031210
            return (int) differenceDays;
        } else {
            throw new IllegalStateException(MenuManagerExceptionMessages.WRONG_DATE);
        }
    }

    // https://api.weatherbit.io/v2.0/forecast/daily?city=Raleigh,NC&key=API_KEY
    // to moze byc logika, ktora powinna byc w dedykowanej klasie, prosze wyekstraktowac
    private CityData getWeather(City city) {
        String url = "https://api.weatherbit.io/v2.0/forecast/daily";
        String apiKey = "c84ad94814fb4457b070f396b4029306";
        String appUrl = url + "?city=" + city.getCityName() + "country=" + city.getCountryCode() + "&key=" + apiKey;
        return restTemplate.getForObject(appUrl, CityData.class);
    }

    // niekonzystentna konwencja nazewnictwa zmiennych snakeCase vs camelCase, powinna byc jedna konwencja w projekcie
    public double bestLocationCalculator(double wind_spd, double temp) {
        return wind_spd * 3 + temp;
    }


}
