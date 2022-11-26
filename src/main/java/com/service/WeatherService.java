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

    public WeatherDataResponse getBestLocation(LocalDate localDate) {
        final List<WeatherDataResponse> result = getWeatherForAllCities(daysToRequestedDate(localDate));
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
        final List<WeatherDataResponse> response = new ArrayList<>();
        final List<City> cities = cityRepository.findAll();
        for (City city : cities) {
            CityData cityData = getWeather(city);
            WeatherDataResponse weatherDataResponse = new WeatherDataResponse(cityData, specificDay);
            response.add(weatherDataResponse);
        }
        return response;
    }

    private int daysToRequestedDate(LocalDate localDate) {
        final LocalDate currentDate = LocalDate.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()));
        final long differenceDays = ChronoUnit.DAYS.between(currentDate, localDate);
        if (differenceDays >= 0 && differenceDays < 16) {
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
    public double bestLocationCalculator(double windSpd, double temp) {
        return windSpd * 3 + temp;
    }


}
