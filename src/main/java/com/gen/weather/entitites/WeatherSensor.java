package com.gen.weather.entitites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;

@Entity(name = "weather_sensors")
public class WeatherSensor extends ManagedEntity {
  @Column(name = "weather_station_id")
  private String weatherStationId;

  @Column(name = "latest_update")
  private LocalDateTime latestUpdate;

  @Column(name = "sensor_status")
  @Enumerated(EnumType.STRING)
  private SensorStatus sensorStatus;

  public String getWeatherStationId() {
    return weatherStationId;
  }

  public void setWeatherStationId(String weatherStationId) {
    this.weatherStationId = weatherStationId;
  }

  public LocalDateTime getLatestUpdate() {
    return latestUpdate;
  }

  public void setLatestUpdate(LocalDateTime latestUpdate) {
    this.latestUpdate = latestUpdate;
  }

  public SensorStatus getSensorStatus() {
    return sensorStatus;
  }

  public void setSensorStatus(SensorStatus sensorStatus) {
    this.sensorStatus = sensorStatus;
  }
}
