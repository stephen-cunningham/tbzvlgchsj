package com.gen.weather.entitites;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class WeatherStationTest {
  @Test
  void gettersAndSetters_ShouldWork() {
    final Double latitude = 53.2714444;
    final Double longitude = -9.0514361;

    WeatherStation weatherStation = new WeatherStation();
    weatherStation.setLatitude(latitude);
    weatherStation.setLongitude(longitude);

    assertAll(
        () -> assertEquals(latitude, weatherStation.getLatitude()),
        () -> assertEquals(longitude, weatherStation.getLongitude())
    );
  }
}