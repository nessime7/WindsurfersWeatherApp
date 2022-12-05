package com.integration;

import com.service.WeatherService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WeatherController.class)
public class WeatherMvcAndWireMockIntegrationTest {

    private MockMvc mvc;
    private WeatherController weatherController;
    private WeatherService weatherService;

    @BeforeAll
    public void setUp() {
        weatherService = Mockito.mock(WeatherService.class);
        weatherController = new WeatherController(weatherService);
        mvc = MockMvcBuilders
                .standaloneSetup(weatherController)
                .build();
    }

    @AfterAll
    public void tearDown() {
        mvc = null;
        weatherController = null;
        weatherService = null;
    }

    @Test
    void get_weather() throws Exception {
        var localDate = java.time.LocalDate.from(java.time.LocalDate.now());
        mvc.perform(MockMvcRequestBuilders
                        .get("/weather/%s", localDate)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

