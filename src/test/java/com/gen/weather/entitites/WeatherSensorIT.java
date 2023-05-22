package com.gen.weather.entitites;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class WeatherSensorIT {
  @Autowired private EntityManager entityManager;

  @Test
  void generatedId_IsNotNull() {
    WeatherSensor weatherSensor = new WeatherSensor();
    entityManager.persist(weatherSensor);

    assertNotNull(weatherSensor.getId());
  }
}
