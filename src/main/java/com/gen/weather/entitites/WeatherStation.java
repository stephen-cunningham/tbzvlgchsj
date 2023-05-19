package com.gen.weather.entitites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity(name = "weather_stations")
public class WeatherStation extends ManagedEntity {
  @Column(name = "latitude")
  private Double latitude;

  @Column(name = "longitude")
  private Double longitude;

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }
}
