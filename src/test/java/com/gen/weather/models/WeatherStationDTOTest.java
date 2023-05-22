package com.gen.weather.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherStationDTOTest {
    @Test
    void constructor() {
        WeatherStationDTO weatherStationDTO = new WeatherStationDTO(53.2714, 9.0489);
        assertAll(
                () -> assertEquals(53.2714, weatherStationDTO.longitude()),
                () -> assertEquals(9.0489, weatherStationDTO.latitude())
        );
    }
}