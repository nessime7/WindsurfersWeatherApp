package com.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.rest.WeatherDataResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@WireMockTest(httpsEnabled = true, httpPort = 8081)
@AutoConfigureMockMvc
public class WeatherRestAssuredAndWireMockIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void should_check_weather_and_return_200() throws Exception {
        // given
        stubFor(get(urlEqualTo("/v2.0/forecast/daily"))
                .withQueryParam("city", equalTo("Jastarnia"))
                .withQueryParam("key", equalTo("c84ad94814fb4457b070f396b4029306"))
                .willReturn(aResponse()
                        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("response/jastarnia-response.json")));

        // when
        var mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/weather/2022-12-06"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // then
        var response = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(UTF_8), WeatherDataResponse.class);

        assertEquals("Jastarnia", response.getCityName());
        verify(getRequestedFor(urlPathEqualTo("/v2.0/forecast/daily")));
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

//    @LocalServerPort
//    private int port;
//    private static final String CONTEXT = "weather";

///////////////////////////////////////

// @ExtendWith(SpringExtension.class)
// @SpringBootTest(classes = {WindsurfersWeatherAppSaraApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

//    stubFor(get(urlEqualTo("/v2.0/forecast/daily?city=Jastarnia&key=c84ad94814fb4457b070f396b4029306")).willReturn(aResponse()
//        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
//        .withBody("response/jastarnia-response.json")));
//        stubFor(get(urlEqualTo("/v2.0/forecast/daily?city=Bridgetown&key=c84ad94814fb4457b070f396b4029306")).willReturn(aResponse()
//        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
//        .withBody("response/bridgetown-response.json")));
//        stubFor(get(urlEqualTo("/v2.0/forecast/daily?city=Fortaleza&key=c84ad94814fb4457b070f396b4029306")).willReturn(aResponse()
//        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
//        .withBody("response/fortaleza-response.json")));
//        stubFor(get(urlEqualTo("/v2.0/forecast/daily?city=Pissouri&key=c84ad94814fb4457b070f396b4029306")).willReturn(aResponse()
//        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
//        .withBody("response/response/pissouri-response.json")));
//        stubFor(get(urlEqualTo("/v2.0/forecast/daily?city=LeMorne&key=c84ad94814fb4457b070f396b4029306")).willReturn(aResponse()
//        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
//        .withBody("response/response/leMorne-response")));
