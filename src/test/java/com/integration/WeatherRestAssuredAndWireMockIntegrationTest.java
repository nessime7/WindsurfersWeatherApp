package com.integration;

import com.TestUtils;
import com.WindsurfersWeatherAppSaraApplication;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {WindsurfersWeatherAppSaraApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
public class WeatherRestAssuredAndWireMockIntegrationTest {

    @LocalServerPort
    private int port;
    private static final String CONTEXT = "weather";

    private static WireMockServer wireMockServer;

    @Autowired
    private MockMvc mockMvc;

    private static String urlToStub;
    private static String stockCode;

    @BeforeAll
    public void setUp() {
    //    RestAssured.port = port;
     //   RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
        WireMock.configureFor("localhost",8081);
        stockCode = "appl";
        urlToStub = String.format("financial-facts/%s", stockCode);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void should_check_weather_and_return_200() throws IOException {
        stubFor(get(urlEqualTo("/v2.0/forecast/daily?city=Jastarnia&key=c84ad94814fb4457b070f396b4029306")).willReturn(aResponse().withBody("response/jastarnia-response.json")));
        stubFor(get(urlEqualTo("/v2.0/forecast/daily?city=Bridgetown&key=c84ad94814fb4457b070f396b4029306")).willReturn(aResponse().withBody("response/bridgetown-response.json")));
        stubFor(get(urlEqualTo("/v2.0/forecast/daily?city=Fortaleza&key=c84ad94814fb4457b070f396b4029306")).willReturn(aResponse().withBody("response/fortaleza-response.json")));
        stubFor(get(urlEqualTo("/v2.0/forecast/daily?city=Pissouri&key=c84ad94814fb4457b070f396b4029306")).willReturn(aResponse().withBody("response/pissouri-response.json")));
        stubFor(get(urlEqualTo("/v2.0/forecast/daily?city=LeMorne&key=c84ad94814fb4457b070f396b4029306")).willReturn(aResponse().withBody("response/leMorne-response.json")));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        given()
                .when().get("/weather/{localDate}", LocalDate.now().format(formatter))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("", equalTo(TestUtils.getPath("response/weather-response.json", CONTEXT).get("")));
    }
}
