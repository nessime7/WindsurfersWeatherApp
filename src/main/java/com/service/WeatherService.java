package com.service;

import com.config.MenuManagerExceptionMessages;
import com.config.WeatherBitApiConnector;
import com.repository.CityRepository;
import com.rest.CityData;
import com.rest.WeatherData;
import com.rest.WeatherDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherService {
    private final WeatherBitApiConnector restTemplateConfig;
    private final CityRepository cityRepository;

    @Autowired
    public WeatherService(WeatherBitApiConnector restTemplateConfig, CityRepository cityRepository) {
        this.restTemplateConfig = restTemplateConfig;
        this.cityRepository = cityRepository;
    }

    /*
    pobieramy miasta
    pobieramy pogody dla miast, czyli filtrujemy pogode dla konkretnego dnia
    wybieramy miasto z najlepsza pogoda i to zwracamy
     */

    // metoda getBestLocation typu WeatherDataResponse z parametrami requestedDate typu LocalDate
    public WeatherDataResponse getBestLocation(LocalDate requestedDate) {
        final var cities = cityRepository.findAll();
        final var weathers = cities.stream()
                .map(restTemplateConfig::getWeather)
                .collect(Collectors.toList());
        return resolveWeather(requestedDate, weathers);
    }

    private WeatherDataResponse resolveWeather(LocalDate requestedDate, List<CityData> weathers) {
        WeatherDataResponse weatherResult = null;
        final var bestLocationValue = 0;
        for (final var weather : weathers) {
            final var weatherForRequestedDate = weatherForRequestedDate(requestedDate, weather);
            if ((weatherForRequestedDate.getTemperature() > 5 && weatherForRequestedDate.getTemperature() < 35) &&
                    (weatherForRequestedDate.getWindSpeed() > 5 && weatherForRequestedDate.getWindSpeed() < 18)) {
                final var currentCityLocationValue = bestLocationCalculator(weatherForRequestedDate.getWindSpeed(),
                        weatherForRequestedDate.getTemperature());
                if (currentCityLocationValue > bestLocationValue) {
                    weatherResult = new WeatherDataResponse(weather.getCityName(),
                            weatherForRequestedDate.getWindSpeed(),
                            weatherForRequestedDate.getTemperature());
                }
            }
        }
        if (weatherResult == null) {
            throw new IllegalStateException(MenuManagerExceptionMessages.NULL_DATA_RESPONSE);
        }
        return weatherResult;
    }

    private WeatherData weatherForRequestedDate(LocalDate requestedDate, CityData weather) {
        return weather.getData().stream()
                .filter(w -> w.getValidDate().equals(requestedDate))
                .findAny()
                .orElseThrow();
    }

    public double bestLocationCalculator(double windSpd, double temp) {
        return windSpd * 3 + temp;
    }
}
