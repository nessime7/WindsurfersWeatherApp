package com.service;

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
    private final WeatherBitApiConnector weatherBitApiConnector;
    private final CityRepository cityRepository;

    @Autowired
    public WeatherService(WeatherBitApiConnector weatherBitApiConnector, CityRepository cityRepository) {
        this.weatherBitApiConnector = weatherBitApiConnector;
        this.cityRepository = cityRepository;
    }

    /*
    pobieramy miasta
    pobieramy pogody dla miast, czyli filtrujemy pogode dla konkretnego dnia
    wybieramy miasto z najlepsza pogoda i to zwracamy
     */

    // metoda getBestLocation typu WeatherDataResponse z parametrami requestedDate typu LocalDate
    public WeatherDataResponse getBestLocation(LocalDate requestedDate) {
        // zmienna cities i przypisanie wyniku metody findAll na polu obiektu cityRepository
        final var cities = cityRepository.findAll();
        // zmienna weathers i zwrócenie wyniku łancucha wywołań na zmiennych cities
        final var weathers = cities.stream()
                // dla każdego miasta pobiera pogodę z 16 dni
                .map(weatherBitApiConnector::getWeather)
                // przypisanie pobranych danych do listy
                .collect(Collectors.toList());
        // zwrócenie wyniku metody resolveWeather o paramatrach requestedDate oraz Listy Weathers
        // czyli mamy naszą datę i nasze miasta z pobraną pogodą WeatherData(temperature, windspd, validdate),
        // oraz info z CityData z 16 dni
        return resolveWeather(requestedDate, weathers);
    }

    // metody resolveWeather, zwraca typ WeatherDataResponse o parametrach requestedDate typu LocalDate
    // oraz weathers typu Lista CityData
    private WeatherDataResponse resolveWeather(LocalDate requestedDate, List<CityData> weathers) {
        // przypisanie do zmiennej typu WeatherDataResponse null
        WeatherDataResponse weatherResult = null;
        // zdefiniowanie zmiennej lokalne bestLocationValue i przypisanie do niej wartości 0
        final var bestLocationValue = 0;
        // dla każdego elementu tablicy Weathers przypisujemy zmienną weather
        for (final var weather : weathers) {
            // zmienna weatherForRequestedDate, przypisanie do niej wyniku metody weatherForRequestedDate
            // o parametrach requestedDate oraz weather
            // czyli mamy pogodę danej daty z danego miasta
            final var weatherForRequestedDate = weatherForRequestedDate(requestedDate, weather);
            if ((weatherForRequestedDate.getTemperature() > 5 && weatherForRequestedDate.getTemperature() < 35) &&
                    (weatherForRequestedDate.getWindSpeed() > 5 && weatherForRequestedDate.getWindSpeed() < 18)) {
                // przypisanie do zmiennej currentCityLocationValue wymiku metody bestLocationCalculator z parametrami
                // getWindSpeed, getTemperature
                final var currentCityLocationValue = bestLocationCalculator(weatherForRequestedDate.getWindSpeed(),
                        weatherForRequestedDate.getTemperature());
                // jeśli wynik zmiennej currentCityLocationValue jest większe niż 0
                if (currentCityLocationValue > bestLocationValue) {
                    // zmienna weatherResult przypisanie nowej odpowiedzi WeatherDataResponse o parametach
                    weatherResult = new WeatherDataResponse(weather.getCityName(),
                            weatherForRequestedDate.getWindSpeed(),
                            weatherForRequestedDate.getTemperature());
                }
            }
        }
        if (weatherResult == null) {
//            throw new IllegalStateException(MenuManagerExceptionMessages.NULL_DATA_RESPONSE);
            throw new IllegalArgumentException("No matching city found.");
        }
        return weatherResult;
    }

    private WeatherData weatherForRequestedDate(LocalDate requestedDate, CityData weather) {
        // zwrócenie wyniku łancucha wywołań na pobranych danych na zmiennej weather typu CityData
        return weather.getData().stream()
                // filtrujemy i sprawdzamy czy getValidateDate jest równe requestedDate
                .filter(w -> w.getValidDate().equals(requestedDate))
                // znajdź pierwszy
                .findAny()
                // lub wyrzuć wyjątek
//                .orElseThrow(() -> new IllegalStateException(MenuManagerExceptionMessages.WRONG_DATE));
                .orElseThrow(() -> new IllegalArgumentException("Wrong date. You can choose 16 day range from today."));
    }

    private double bestLocationCalculator(double windSpd, double temp) {
        return windSpd * 3 + temp;
    }
}

// jeśli inna klasa potrzebuje dostępu do mojej metody, to musi być publiczna
