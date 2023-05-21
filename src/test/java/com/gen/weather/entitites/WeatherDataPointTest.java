package com.gen.weather.entitites;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherDataPointTest {
  @Test
  void gettersAndSetters_ShouldWork() {
    final UUID weatherSensorId = UUID.randomUUID();
    final MetricType metricType = MetricType.HUMIDITY;
    final Double metricValue = 20.1;

    WeatherDataPoint weatherDataPoint = new WeatherDataPoint();
    weatherDataPoint.setWeatherSensorId(weatherSensorId);
    weatherDataPoint.setMetricType(metricType);
    weatherDataPoint.setMetricValue(metricValue);

    assertAll(
        () -> assertEquals(weatherSensorId, weatherDataPoint.getWeatherSensorId()),
        () -> assertEquals(metricType, weatherDataPoint.getMetricType()),
        () -> assertEquals(metricValue, weatherDataPoint.getMetricValue())
    );
  }
}