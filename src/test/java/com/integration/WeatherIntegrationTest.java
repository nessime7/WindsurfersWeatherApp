package com.integration;

import com.TestUtils;
import com.WindsurfersWeatherAppSaraApplication;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

// testy nie dzialaja, prosze poczytac o bibliotece wiremock i sprobowac jej uzyc
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {WindsurfersWeatherAppSaraApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        given()
                .when().get("/weather/{data}", LocalDate.now().format(formatter))
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