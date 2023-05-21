package com.gen.weather.entitites;

import com.gen.weather.models.WeatherSensorCreateDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "weather_sensors")
public class WeatherSensor extends IdentifiableEntity {
  @Column(name = "weather_station_id", updatable = false)
  private UUID weatherStationId;

  @Column(name = "last_data_creation_timestamp")
  private LocalDateTime lastDataCreationTimestamp;

  @Column(name = "sensor_status", nullable = false)
  @Enumerated(EnumType.STRING)
  private SensorStatus sensorStatus = SensorStatus.LIVE;

  public UUID getWeatherStationId() {
    return weatherStationId;
  }

  public void setWeatherStationId(UUID weatherStationId) {
    this.weatherStationId = weatherStationId;
  }

  public LocalDateTime getLastDataCreationTimestamp() {
    return lastDataCreationTimestamp;
  }

  public void setLastDataCreationTimestamp(LocalDateTime lastDataCreationTimestamp) {
    this.lastDataCreationTimestamp = lastDataCreationTimestamp;
  }

  public SensorStatus getSensorStatus() {
    return sensorStatus;
  }

  public void setSensorStatus(SensorStatus sensorStatus) {
    this.sensorStatus = sensorStatus;
  }
}
