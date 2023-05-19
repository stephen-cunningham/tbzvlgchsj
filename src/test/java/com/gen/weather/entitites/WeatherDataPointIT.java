package com.gen.weather.entitites;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class WeatherDataPointIT {
  @Autowired private EntityManager entityManager;

  @Test
  void generatedId_IsNotNull() {
    WeatherDataPoint weatherDataPoint = new WeatherDataPoint();
    entityManager.persist(weatherDataPoint);

    assertNotNull(weatherDataPoint.getId());
  }
}
