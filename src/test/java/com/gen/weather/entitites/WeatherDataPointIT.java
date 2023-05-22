package com.gen.weather.entitites;

import com.gen.weather.configuration.AuditConfiguration;
import com.gen.weather.repositories.WeatherDataPointRepository;
import com.gen.weather.services.WeatherSensorService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import(AuditConfiguration.class)
public class WeatherDataPointIT {
  @Autowired private EntityManager entityManager;
  @Autowired private WeatherDataPointRepository weatherDataPointRepository;
  @MockBean private WeatherSensorService weatherSensorService;

  @Test
  void generatedValues_AreNotNull() {
    WeatherDataPoint weatherDataPoint = new WeatherDataPoint();
    entityManager.persist(weatherDataPoint);

    assertAll(
        () -> assertNotNull(weatherDataPoint.getId()),
        () -> assertNotNull(weatherDataPoint.getMetricTimestamp())
    );
  }
}
