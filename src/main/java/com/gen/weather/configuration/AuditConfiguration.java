package com.gen.weather.configuration;

import java.time.ZonedDateTime;
import java.util.Optional;

import com.gen.weather.providers.WeatherSensorUpdateAspect;
import com.gen.weather.repositories.WeatherSensorRepository;
import com.gen.weather.services.WeatherSensorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
@EnableAspectJAutoProxy
public class AuditConfiguration {
  @Bean
  public DateTimeProvider auditingDateTimeProvider() {
    return () -> Optional.of(ZonedDateTime.now());
  }

  @Bean
  public WeatherSensorUpdateAspect weatherSensorUpdateAspect(WeatherSensorRepository weatherSensorRepository, WeatherSensorService weatherSensorService) {
    return new WeatherSensorUpdateAspect(weatherSensorRepository, weatherSensorService);
  }
}
