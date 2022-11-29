package com.service;

import com.config.MenuManagerExceptionMessages;
import com.config.WeatherBitApiConnector;
import com.repository.CityRepository;
import com.rest.WeatherDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {
    private final WeatherBitApiConnector restTemplateConfig;
    private final CityRepository cityRepository;

    @Autowired
    public WeatherService(WeatherBitApiConnector restTemplateConfig, CityRepository cityRepository) {
        this.restTemplateConfig = restTemplateConfig;
        this.cityRepository = cityRepository;
    }

    public WeatherDataResponse getBestLocation(LocalDate localDate) {
        final List<WeatherDataResponse> result = getWeatherForAllCities(daysToRequestedDate(localDate));
        WeatherDataResponse weatherResult = null;
        final var bestLocationValue = 0;
        for (WeatherDataResponse item : result) {
            if ((item.getTemperature() > 5 && item.getTemperature() < 35) &&
                    (item.getWindSpeed() > 5 && item.getWindSpeed() < 18)) {
                final var currentCityLocationValue = bestLocationCalculator(item.getWindSpeed(), item.getTemperature());
                if (currentCityLocationValue > bestLocationValue) {
                    weatherResult = item;
                }
            }
        }
        if (weatherResult == null) {
            throw new IllegalStateException(MenuManagerExceptionMessages.NULL_DATA_RESPONSE);
        }
        return weatherResult;
//        return result.stream()
//                .filter(city -> city.getTemp() > 5 && city.getTemp() < 35)
//                .filter(city -> city.getWind_spd() > 5 && city.getWind_spd() < 18)
//                .max(Comparator.comparing(weather -> bestLocationCalculator(weather.getWind_spd(), weather.getTemp())))
//                .orElse(null);
    }

    private List<WeatherDataResponse> getWeatherForAllCities(int specificDay) {
        final var response = new ArrayList<WeatherDataResponse>();
        final var cities = cityRepository.findAll();
        for (var city : cities) {
            final var cityName = restTemplateConfig.getWeather(city).getCityName();
            final var countryCode = restTemplateConfig.getWeather(city).getCountryCode();
            final var data = restTemplateConfig.getWeather(city).getData()[specificDay];
            final var windSpeed = data.getWindSpeed();
            final var temperature = data.getTemperature();
            final var weatherDataResponse = new WeatherDataResponse(cityName, countryCode, windSpeed, temperature);
            response.add(weatherDataResponse);
        }
        return response;
    }
// get current date
// count how many days until request date
// get current date + offset from diff data


// fo through all data entries
// filter by valid date
    private int daysToRequestedDate(LocalDate localDate) {
        final var currentDate = LocalDate.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()));
        final var differenceDays = ChronoUnit.DAYS.between(currentDate, localDate);
        if (differenceDays >= 0 && differenceDays < 16) {
            return (int) differenceDays;
        } else {
            throw new IllegalStateException(MenuManagerExceptionMessages.WRONG_DATE);
        }
    }

    public double bestLocationCalculator(double windSpd, double temp) {
        return windSpd * 3 + temp;
    }
}
