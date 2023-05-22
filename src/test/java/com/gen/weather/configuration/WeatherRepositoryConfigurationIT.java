package com.gen.weather.configuration;

import com.gen.weather.utils.TestUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherRepositoryConfigurationIT {
    @Autowired private MockMvc mockMvc;

    @Test
    void configureRepositoryRestConfiguration_RunApplication_WeatherDataPointIdsExposed() throws Exception {
        assertIdPresent("weatherDataPoints");
    }

    void assertIdPresent(String data) throws Exception {
        MockHttpServletResponse response = TestUtils.performGetRequest(mockMvc, "/" + data);
        JSONObject responseJson = new JSONObject(response.getContentAsString());
        JSONObject embeddedJson = (JSONObject) responseJson.get("_embedded");
        JSONArray results = (JSONArray) embeddedJson.get(data);
        JSONObject firstData = (JSONObject) results.get(0);

        assertNotNull(firstData.get("id"));
    }

    @Test
    void configureRepositoryRestConfiguration_RunApplication_WeatherSensorIdsExposed() throws Exception {
        assertIdPresent("weatherSensors");
    }

    @Test
    void configureRepositoryRestConfiguration_RunApplication_WeatherStationIdsExposed() throws Exception {
        assertIdPresent("weatherStations");
    }
}