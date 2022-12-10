package com.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@WireMockTest(httpPort = 8081)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class WeatherRestAssuredAndWireMockIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void should_check_weather_and_return_200_from_today() throws Exception {
        // given
        stubFor(get("/v2.0/forecast/daily?city=Jastarnia&key=c84ad94814fb4457b070f396b4029306")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                new JsonPath(ResourceUtils.getFile("classpath:weather/response/jastarnia-response.json"))
                                        .prettyPrint())
                )
        );
        stubFor(get("/v2.0/forecast/daily?city=Bridgetown&key=c84ad94814fb4457b070f396b4029306")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                new JsonPath(ResourceUtils.getFile("classpath:weather/response/bridgetown-response.json"))
                                        .prettyPrint())
                )
        );
        stubFor(get("/v2.0/forecast/daily?city=Fortaleza&key=c84ad94814fb4457b070f396b4029306")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                new JsonPath(ResourceUtils.getFile("classpath:weather/response/fortaleza-response.json"))
                                        .prettyPrint())
                )
        );
        stubFor(get("/v2.0/forecast/daily?city=Le%20Morne&key=c84ad94814fb4457b070f396b4029306")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                new JsonPath(ResourceUtils.getFile("classpath:weather/response/leMorne-response.json"))
                                        .prettyPrint())
                )
        );
        stubFor(get("/v2.0/forecast/daily?city=Pissouri&key=c84ad94814fb4457b070f396b4029306")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                new JsonPath(ResourceUtils.getFile("classpath:weather/response/pissouri-response.json"))
                                        .prettyPrint())
                )
        );

        // then
        var mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/weather/2022-12-10"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(content().string("{\"message\":\"No matching city found.\"}"))
                .andReturn();
    }

    @Test
    void should_return_500_when_date_is_from_past() throws Exception {
        // given
        stubFor(get("/v2.0/forecast/daily?city=Jastarnia&key=c84ad94814fb4457b070f396b4029306")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                new JsonPath(ResourceUtils.getFile("classpath:weather/response/jastarnia-response.json"))
                                        .prettyPrint())
                )
        );
        stubFor(get("/v2.0/forecast/daily?city=Bridgetown&key=c84ad94814fb4457b070f396b4029306")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                new JsonPath(ResourceUtils.getFile("classpath:weather/response/bridgetown-response.json"))
                                        .prettyPrint())
                )
        );
        stubFor(get("/v2.0/forecast/daily?city=Fortaleza&key=c84ad94814fb4457b070f396b4029306")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                new JsonPath(ResourceUtils.getFile("classpath:weather/response/fortaleza-response.json"))
                                        .prettyPrint())
                )
        );
        stubFor(get("/v2.0/forecast/daily?city=Le%20Morne&key=c84ad94814fb4457b070f396b4029306")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                new JsonPath(ResourceUtils.getFile("classpath:weather/response/leMorne-response.json"))
                                        .prettyPrint())
                )
        );
        stubFor(get("/v2.0/forecast/daily?city=Pissouri&key=c84ad94814fb4457b070f396b4029306")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                new JsonPath(ResourceUtils.getFile("classpath:weather/response/pissouri-response.json"))
                                        .prettyPrint())
                )
        );

        // then
        var mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/weather/2022-12-06"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(content().string("{\"message\":\"Wrong date. You can choose 16 day range from today.\"}"))
                .andReturn();
    }

    @Test
    void should_return_500_when_date_is_from_future() throws Exception {
        // given
        stubFor(get("/v2.0/forecast/daily?city=Jastarnia&key=c84ad94814fb4457b070f396b4029306")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                new JsonPath(ResourceUtils.getFile("classpath:weather/response/jastarnia-response.json"))
                                        .prettyPrint())
                )
        );
        stubFor(get("/v2.0/forecast/daily?city=Bridgetown&key=c84ad94814fb4457b070f396b4029306")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                new JsonPath(ResourceUtils.getFile("classpath:weather/response/bridgetown-response.json"))
                                        .prettyPrint())
                )
        );
        stubFor(get("/v2.0/forecast/daily?city=Fortaleza&key=c84ad94814fb4457b070f396b4029306")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                new JsonPath(ResourceUtils.getFile("classpath:weather/response/fortaleza-response.json"))
                                        .prettyPrint())
                )
        );
        stubFor(get("/v2.0/forecast/daily?city=Le%20Morne&key=c84ad94814fb4457b070f396b4029306")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                new JsonPath(ResourceUtils.getFile("classpath:weather/response/leMorne-response.json"))
                                        .prettyPrint())
                )
        );
        stubFor(get("/v2.0/forecast/daily?city=Pissouri&key=c84ad94814fb4457b070f396b4029306")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                new JsonPath(ResourceUtils.getFile("classpath:weather/response/pissouri-response.json"))
                                        .prettyPrint())
                )
        );

        // then
        var mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/weather/2023-12-06"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(content().string("{\"message\":\"Wrong date. You can choose 16 day range from today.\"}"))
                .andReturn();
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

//    // then
//    var response = objectMapper.readValue(mvcResult.getResponse()
//            .getContentAsString(UTF_8), WeatherDataResponse.class);
//    assertEquals("Jastarnia", response.getCityName());
//        assertEquals(20.0, response.getTemperature());
//        assertEquals(12.0, response.getWindSpeed());

// 500, coś się stało co było nieoczekiwane, nie ma żadnego mechanizmu który by to rozwiązywał,
// w idealnym świecie nasz program obsługuje te złe scenariusze, musi zwracać 403, więc najlepiej w serwisie obsłużyć wszystkie możliwości