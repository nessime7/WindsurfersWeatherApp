package com.integration;

import com.WindsurfersWeatherAppSaraApplication;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {WindsurfersWeatherAppSaraApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 8081)
@AutoConfigureMockMvc
public class WeatherRestAssuredAndWireMockIntegrationTest {

    @LocalServerPort
    private int port;
    private static final String CONTEXT = "weather";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_check_weather_and_return_200() throws Exception {
        stubFor(get(urlEqualTo("/v2.0/forecast/daily?city=Jastarnia&key=c84ad94814fb4457b070f396b4029306")).willReturn(aResponse()
                .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("response/jastarnia-response.json")));
        stubFor(get(urlEqualTo("/v2.0/forecast/daily?city=Bridgetown&key=c84ad94814fb4457b070f396b4029306")).willReturn(aResponse()
                .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("response/bridgetown-response.json")));
        stubFor(get(urlEqualTo("/v2.0/forecast/daily?city=Fortaleza&key=c84ad94814fb4457b070f396b4029306")).willReturn(aResponse()
                .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("response/fortaleza-response.json")));
        stubFor(get(urlEqualTo("/v2.0/forecast/daily?city=Pissouri&key=c84ad94814fb4457b070f396b4029306")).willReturn(aResponse()
                .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("response/response/pissouri-response.json")));
        stubFor(get(urlEqualTo("/v2.0/forecast/daily?city=LeMorne&key=c84ad94814fb4457b070f396b4029306")).willReturn(aResponse()
                .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("response/response/leMorne-response")));

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/weather/2022-12-05")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}

//        wywołanie mojego API używając restAssured
//        given()
//                .when().get("/weather/{localDate}", LocalDate.now().format(formatter))
//                .then()
//                .statusCode(HttpStatus.SC_OK)
//                .and()
//                .body("", equalTo(TestUtils.getPath("response/weather-response.json", CONTEXT).get("")));
//         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
