package com.gen.weather.entitites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity(name = "weather_data_points")
public class WeatherDataPoint extends ManagedEntity {
  @Column(name = "weather_sensor_id")
  private String weatherSensorId;

  @Column(name = "metric_type")
  @Enumerated(EnumType.STRING)
  private MetricType metricType;

  @Column(name = "metric_value")
  private Double metricValue;

  public String getWeatherSensorId() {
    return weatherSensorId;
  }

  public void setWeatherSensorId(String weatherSensorId) {
    this.weatherSensorId = weatherSensorId;
  }

  public MetricType getMetricType() {
    return metricType;
  }

  public void setMetricType(MetricType metricType) {
    this.metricType = metricType;
  }

  public Double getMetricValue() {
    return metricValue;
  }

  public void setMetricValue(Double metricValue) {
    this.metricValue = metricValue;
  }
}
