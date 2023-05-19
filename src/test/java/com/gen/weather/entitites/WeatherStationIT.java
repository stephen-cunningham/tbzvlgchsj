package com.gen.weather.entitites;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class WeatherStationIT {
  @Autowired private EntityManager entityManager;

  @Test
  void generatedId_IsNotNull() {
    WeatherStation weatherStation = new WeatherStation();
    entityManager.persist(weatherStation);

    assertNotNull(weatherStation.getId());
  }
}
