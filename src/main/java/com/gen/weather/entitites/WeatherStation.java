package com.gen.weather.entitites;

import com.gen.weather.models.WeatherStationDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity(name = "weather_stations")
public class WeatherStation extends IdentifiableEntity {
  @Column(nullable = false)
  private Double latitude;

  @Column(nullable = false)
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

  public static WeatherStation from(WeatherStationDTO weatherStationDTO) {
    WeatherStation weatherStation = new WeatherStation();
    weatherStation.setLongitude(weatherStationDTO.longitude());
    weatherStation.setLatitude(weatherStationDTO.latitude());

    return weatherStation;
  }
}
