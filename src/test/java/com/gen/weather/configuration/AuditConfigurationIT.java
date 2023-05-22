package com.gen.weather.configuration;

import com.gen.weather.entitites.MetricType;
import com.gen.weather.entitites.WeatherDataPoint;
import com.gen.weather.providers.WeatherSensorUpdateAspect;
import com.gen.weather.repositories.WeatherDataPointRepository;
import com.gen.weather.services.WeatherSensorService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.DateTimeProvider;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
class AuditConfigurationIT {
    @Autowired private DateTimeProvider dateTimeProvider;
    @Autowired private WeatherSensorUpdateAspect weatherSensorUpdateAspect;
    @Autowired private WeatherDataPointRepository weatherDataPointRepository;

    @MockBean private WeatherSensorService weatherSensorService;

    @Captor private ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor private ArgumentCaptor<LocalDateTime> localDateTimeArgumentCaptor;

    @Test
    void beansExist() {
        assertAll(
                () -> assertNotNull(dateTimeProvider),
                () -> assertNotNull(weatherSensorUpdateAspect)
        );
    }

    @Test
    void auditingDateTimeProvider_UseIt_ReturnsCurrentTime() {
        LocalDateTime timeBeforeCall = LocalDateTime.now().minusSeconds(1);
        Optional<TemporalAccessor> timeOfCallOptional = dateTimeProvider.getNow();
        LocalDateTime timeAfterCall = LocalDateTime.now().plusSeconds(1);

        assertTrue(timeOfCallOptional.isPresent());
        LocalDateTime timeOfCall = LocalDateTime.from(timeOfCallOptional.get());
        assertTrue(timeOfCall.isAfter(timeBeforeCall) && timeOfCall.isBefore(timeAfterCall));
    }

    @Test
    void weatherSensorUpdateAspect_UseIt_UpdatesWeatherSensor() {
        UUID weatherSensorId = UUID.randomUUID();
        LocalDateTime timeBeforeCall = LocalDateTime.now().minusSeconds(1);
        WeatherDataPoint weatherDataPoint = new WeatherDataPoint(weatherSensorId, MetricType.HUMIDITY, 2.0);
        LocalDateTime timeAfterCall = LocalDateTime.now().plusSeconds(1);
        weatherDataPointRepository.save(weatherDataPoint);

        assertAll(
                () -> verify(weatherSensorService).updateLastDataCreationTimestamp(uuidArgumentCaptor.capture(), localDateTimeArgumentCaptor.capture()),
                () -> assertEquals(weatherSensorId, uuidArgumentCaptor.getValue()),
                () -> {
                    LocalDateTime lastDataCreationTime = localDateTimeArgumentCaptor.getValue();
                    assertTrue(lastDataCreationTime.isAfter(timeBeforeCall) && lastDataCreationTime.isBefore(timeAfterCall));
                }
        );
    }
}