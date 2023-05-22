package com.gen.weather.entitites;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherDataPointTest {
  private static final UUID WEATHER_SENSOR_ID = UUID.randomUUID();
  private static final MetricType METRIC_TYPE = MetricType.HUMIDITY;
  private static final Double METRIC_VALUE = 20.1;

  private WeatherDataPoint weatherDataPoint;

  @Test
  void overloadedConstructor() {
    assertFieldsSetCorrectly(new WeatherDataPoint(WEATHER_SENSOR_ID, METRIC_TYPE, METRIC_VALUE));
  }

  private void assertFieldsSetCorrectly(WeatherDataPoint weatherDataPoint) {
    assertAll(
            () -> assertEquals(WEATHER_SENSOR_ID, weatherDataPoint.getWeatherSensorId()),
            () -> assertEquals(METRIC_TYPE, weatherDataPoint.getMetricType()),
            () -> assertEquals(METRIC_VALUE, weatherDataPoint.getMetricValue())
    );
  }

  @Test
  void gettersAndSetters() {
    WeatherDataPoint weatherDataPoint = new WeatherDataPoint();
    weatherDataPoint.setWeatherSensorId(WEATHER_SENSOR_ID);
    weatherDataPoint.setMetricType(METRIC_TYPE);
    weatherDataPoint.setMetricValue(METRIC_VALUE);

    assertFieldsSetCorrectly(weatherDataPoint);
  }
}