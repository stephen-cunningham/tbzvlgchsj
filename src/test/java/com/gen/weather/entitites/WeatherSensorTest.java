package com.gen.weather.entitites;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class WeatherSensorTest {
  @Test
  void gettersAndSetters_ShouldWork() {
    final String weatherStationId = UUID.randomUUID().toString();
    final LocalDateTime  latestUpdate = LocalDateTime.now();
    final SensorStatus sensorStatus = SensorStatus.FAULT;

    WeatherSensor weatherSensor = new WeatherSensor();
    weatherSensor.setLatestUpdate(latestUpdate);
    weatherSensor.setSensorStatus(sensorStatus);
    weatherSensor.setWeatherStationId(weatherStationId);

    assertAll(
        () -> assertEquals(weatherStationId, weatherSensor.getWeatherStationId()),
        () -> assertEquals(latestUpdate, weatherSensor.getLatestUpdate()),
        () -> assertEquals(sensorStatus, weatherSensor.getSensorStatus())
    );
  }
}