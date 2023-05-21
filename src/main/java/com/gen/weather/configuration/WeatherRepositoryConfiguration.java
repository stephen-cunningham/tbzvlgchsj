package com.gen.weather.configuration;

import com.gen.weather.entitites.WeatherDataPoint;
import com.gen.weather.entitites.WeatherSensor;
import com.gen.weather.entitites.WeatherStation;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class WeatherRepositoryConfiguration implements RepositoryRestConfigurer {

  @Autowired
  private EntityManager entityManager;

  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
    config.exposeIdsFor(WeatherDataPoint.class, WeatherSensor.class, WeatherStation.class);
  }
}