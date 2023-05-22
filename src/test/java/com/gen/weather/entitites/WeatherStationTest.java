package com.gen.weather.entitites;

import com.gen.weather.models.WeatherStationDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherStationTest {
  @Mock private WeatherStationDTO weatherStationDTO;

  @Test
  void gettersAndSetters() {
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

  @Test
  void from_MockedDTO_SetFieldsCorrectly() {
    when(weatherStationDTO.latitude()).thenReturn(1.0);
    when(weatherStationDTO.longitude()).thenReturn(-1.0);

    WeatherStation weatherStation = WeatherStation.from(weatherStationDTO);

    assertAll(
            () -> assertEquals(1.0, weatherStation.getLatitude()),
            () -> assertEquals(-1.0, weatherStation.getLongitude())
    );
  }
}