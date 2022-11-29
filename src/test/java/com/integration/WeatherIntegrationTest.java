package com.integration;

import com.TestUtils;
import com.WindsurfersWeatherAppSaraApplication;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

/*
Jak dziala program:
1. Request do windsurfersweatherappsara
2. Request do weather-bit API
3. Procesowanie odpowiedzi
4. Odpowiedz do uzytkownika(mozna zobacyzc w postaman lub w przegladarce)

Jaki scenariusz chcemy przetestowac?
1. Request do windsurfersweatherappsara
2. Request do zamockowanego weather-bit API
3. Procesowanie odpowiedzi
4. Odpowiedz do uzytkownika(mozna zobacyzc w postaman lub w przegladarce)

Wiremock uruchamia serwer do testowania
1. localhost:8081
 */

// testy nie dzialaja, prosze poczytac o bibliotece wiremock i sprobowac jej uzyc
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {WindsurfersWeatherAppSaraApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WireMockTest(httpPort = 8081)
public class WeatherIntegrationTest {

    @LocalServerPort
    private int port;
    private static final String CONTEXT = "weather";

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void should_check_weather_and_return_200() throws FileNotFoundException {
// brakujace given, brak var
        /*
                String url = "https://api.weatherbit.io/v2.0/forecast/daily";
        String apiKey = "c84ad94814fb4457b070f396b4029306";
        String appUrl = https://api.weatherbit.io/v2.0/forecast/daily?city=" + city.getCityName() + "country=" + city.getCountryCode() + "&key=" + apiKey;

https://api.weatherbit.io/v2.0/forecast/daily?city=Jastarnia&key=c84ad94814fb4457b070f396b4029306
https://api.weatherbit.io/v2.0/forecast/daily?city=Bridgetown&key=c84ad94814fb4457b070f396b4029306
https://api.weatherbit.io/v2.0/forecast/daily?city=Fortaleza&key=c84ad94814fb4457b070f396b4029306
https://api.weatherbit.io/v2.0/forecast/daily?city=Pissouri&key=c84ad94814fb4457b070f396b4029306
https://api.weatherbit.io/v2.0/forecast/daily?city=Le Morne&key=c84ad94814fb4457b070f396b4029306
->
[host:port/path]
localhost:8081/v2.0/forecast/daily?city=Jastarnia&key=c84ad94814fb4457b070f396b4029306
localhost:8081/v2.0/forecast/daily?city=Bridgetown&key=c84ad94814fb4457b070f396b4029306
localhost:8081/v2.0/forecast/daily?city=Fortaleza&key=c84ad94814fb4457b070f396b4029306
localhost:8081/v2.0/forecast/daily?city=Pissouri&key=c84ad94814fb4457b070f396b4029306
localhost:8081/v2.0/forecast/daily?city=Le Morne&key=c84ad94814fb4457b070f396b4029306

         */
        stubFor(WireMock.get("/v2.0/forecast/daily?city=Jastarnia&key=c84ad94814fb4457b070f396b4029306").willReturn(ok()));
        stubFor(WireMock.get("/v2.0/forecast/daily?city=Bridgetown&key=c84ad94814fb4457b070f396b4029306").willReturn(ok()));
        stubFor(WireMock.get("/v2.0/forecast/daily?city=Fortaleza&key=c84ad94814fb4457b070f396b4029306").willReturn(ok()));
        stubFor(WireMock.get("/v2.0/forecast/daily?city=Pissouri&key=c84ad94814fb4457b070f396b4029306").willReturn(ok()));
        stubFor(WireMock.get("/v2.0/forecast/daily?city=Le Morne&key=c84ad94814fb4457b070f396b4029306").willReturn(ok()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDate.of(2022, 11, 28).format(formatter);
        given()
                .when().get("/weather/{data}", date)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("", equalTo(TestUtils.getPath("response/get-weather-from-today.json", CONTEXT).get("")));
    }

    @Test
    void should_not_check_weather_and_return_500() {
// brak var
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        given()
                .when().get("/weather/{data}", LocalDate.of(2222, 11, 21).format(formatter))
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .and().body("message", is("Wrong date. You can choose 16 day range from today."));
    }
}