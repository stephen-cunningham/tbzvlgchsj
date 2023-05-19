package com.gen.weather.entitites;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class WeatherDataPointTest {
  @Test
  void gettersAndSetters_ShouldWork() {
    final String weatherSensorId = UUID.randomUUID().toString();
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