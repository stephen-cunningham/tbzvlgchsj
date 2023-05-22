package com.gen.weather.models;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WeatherSensorCreateDTOTest {
    @Test
    void constructor() {
        UUID uuid = UUID.randomUUID();
        WeatherSensorCreateDTO weatherSensorCreateDTO = new WeatherSensorCreateDTO(uuid);
        assertEquals(uuid, weatherSensorCreateDTO.weatherStationId());
    }
}