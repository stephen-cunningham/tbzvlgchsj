package com.gen.weather.models;

import com.gen.weather.entitites.MetricType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WeatherDataPointDTOTest {
    @Test
    void constructor() {
        UUID uuid = UUID.randomUUID();
        WeatherDataPointDTO weatherDataPointDTO = new WeatherDataPointDTO(uuid, MetricType.HUMIDITY, 85.01);
        assertAll(
                () -> assertEquals(uuid, weatherDataPointDTO.weatherSensorId()),
                () -> assertEquals(MetricType.HUMIDITY, weatherDataPointDTO.metricType()),
                () -> assertEquals(85.01, weatherDataPointDTO.metricValue())
        );
    }
}