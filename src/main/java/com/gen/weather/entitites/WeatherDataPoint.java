package com.gen.weather.entitites;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "weather_data_points")
@EntityListeners(AuditingEntityListener.class)
public class WeatherDataPoint extends IdentifiableEntity {
  @Column(name = "weather_sensor_id", nullable = false)
  private UUID weatherSensorId;

  @Column(name = "metric_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private MetricType metricType;

  @Column(name = "metric_value", nullable = false)
  private Double metricValue;

  @Column(name = "metric_timestamp", nullable = false)
  @CreatedDate
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private LocalDateTime metricTimestamp;

  public WeatherDataPoint() {}

  public WeatherDataPoint(UUID weatherSensorId, MetricType metricType, Double metricValue) {
    this.weatherSensorId = weatherSensorId;
    this.metricType = metricType;
    this.metricValue = metricValue;
  }

  public UUID getWeatherSensorId() {
    return weatherSensorId;
  }

  public void setWeatherSensorId(UUID weatherSensorId) {
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

  public LocalDateTime getMetricTimestamp() {
    return metricTimestamp;
  }

  public void setMetricTimestamp(LocalDateTime metricTimestamp) {
    this.metricTimestamp = metricTimestamp;
  }
}
