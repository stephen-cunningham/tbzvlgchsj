package com.gen.weather.entitites;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherSensorTest {
  @Test
  void gettersAndSetters_ShouldWork() {
    final UUID weatherStationId = UUID.randomUUID();
    final LocalDateTime  latestUpdate = LocalDateTime.now();
    final SensorStatus sensorStatus = SensorStatus.FAULT;

    WeatherSensor weatherSensor = new WeatherSensor();
    weatherSensor.setLastDataCreationTimestamp(latestUpdate);
    weatherSensor.setSensorStatus(sensorStatus);
    weatherSensor.setWeatherStationId(weatherStationId);

    assertAll(
        () -> assertEquals(weatherStationId, weatherSensor.getWeatherStationId()),
        () -> assertEquals(latestUpdate, weatherSensor.getLastDataCreationTimestamp()),
        () -> assertEquals(sensorStatus, weatherSensor.getSensorStatus())
    );
  }
}