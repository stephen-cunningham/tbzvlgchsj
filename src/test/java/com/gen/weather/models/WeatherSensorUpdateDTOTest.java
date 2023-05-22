package com.gen.weather.models;

import com.gen.weather.entitites.SensorStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherSensorUpdateDTOTest {
    @Test
    void constructor() {
        UUID uuid = UUID.randomUUID();
        WeatherSensorUpdateDTO weatherSensorUpdateDTO = new WeatherSensorUpdateDTO(uuid, SensorStatus.FAULT);
        assertAll(
                () -> assertEquals(uuid, weatherSensorUpdateDTO.id()),
                () -> assertEquals(SensorStatus.FAULT, weatherSensorUpdateDTO.sensorStatus())
        );
    }
}