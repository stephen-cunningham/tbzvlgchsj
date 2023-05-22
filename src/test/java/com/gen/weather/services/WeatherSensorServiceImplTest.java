package com.gen.weather.services;

import com.gen.weather.entitites.SensorStatus;
import com.gen.weather.entitites.WeatherSensor;
import com.gen.weather.entitites.WeatherStation;
import com.gen.weather.repositories.WeatherSensorRepository;
import com.gen.weather.repositories.WeatherStationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherSensorServiceImplTest {
    private WeatherSensorServiceImpl weatherSensorService;

    @Mock private WeatherSensorRepository weatherSensorRepository;
    @Mock private WeatherStationRepository weatherStationRepository;
    @Mock private WeatherStation weatherStation;

    @Captor private ArgumentCaptor<WeatherSensor> weatherSensorArgumentCaptor;

    @BeforeEach
    void setUp() {
        weatherSensorService = new WeatherSensorServiceImpl(weatherSensorRepository, weatherStationRepository);
    }

    @Test
    void save_WeatherStationNotFound_ThrowsError() {
        when(weatherStationRepository.findById(any())).thenReturn(Optional.empty());
        UUID uuid = UUID.randomUUID();

        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> weatherSensorService.save(uuid));
        assertEquals("Weather Station does not exist with id " + uuid, throwable.getMessage());
        verifyNoInteractions(weatherSensorRepository);
    }

    @Test
    void save_WeatherStationFound_Saves() {
        when(weatherStationRepository.findById(any())).thenReturn(Optional.of(weatherStation));
        UUID uuid = UUID.randomUUID();

        weatherSensorService.save(uuid);

        verify(weatherSensorRepository).save(weatherSensorArgumentCaptor.capture());
        WeatherSensor weatherSensor = weatherSensorArgumentCaptor.getValue();
        assertEquals(uuid, weatherSensor.getWeatherStationId());
    }

    @Test
    void updateStatus_WeatherSensorNotFound_ThrowsError() {
        when(weatherSensorRepository.findById(any())).thenReturn(Optional.empty());
        UUID uuid = UUID.randomUUID();

        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> weatherSensorService.updateStatus(uuid, SensorStatus.FAULT));
        assertEquals("Weather Sensor does not exist with id " + uuid, throwable.getMessage());
        verify(weatherSensorRepository, times(0)).save(any());
    }

    @Test
    void updateStatus_WeatherSensorFound_Saves() {
        WeatherSensor weatherSensor = new WeatherSensor();
        weatherSensor.setSensorStatus(SensorStatus.LIVE);
        UUID uuid = UUID.randomUUID();

        when(weatherSensorRepository.findById(any())).thenReturn(Optional.of(weatherSensor));

        weatherSensorService.updateStatus(uuid, SensorStatus.RETIRED);
        verify(weatherSensorRepository).save(weatherSensorArgumentCaptor.capture());
        assertEquals(SensorStatus.RETIRED, weatherSensorArgumentCaptor.getValue().getSensorStatus());
    }

    @Test
    void updateLastDataCreationTimestamp_WeatherSensorNotFound_DoesntSave() {
        when(weatherSensorRepository.findById(any())).thenReturn(Optional.empty());

        weatherSensorService.updateLastDataCreationTimestamp(UUID.randomUUID(), LocalDateTime.now());

        verify(weatherSensorRepository, times(0)).save(any());
    }

    @Test
    void updateLastDataCreationTimestamp_WeatherSensorFound_Saves() {
        WeatherSensor weatherSensor = new WeatherSensor();
        LocalDateTime timestamp = LocalDateTime.now();

        when(weatherSensorRepository.findById(any())).thenReturn(Optional.of(weatherSensor));

        weatherSensorService.updateLastDataCreationTimestamp(UUID.randomUUID(), timestamp);
        verify(weatherSensorRepository).save(weatherSensorArgumentCaptor.capture());
        assertEquals(timestamp, weatherSensorArgumentCaptor.getValue().getLastDataCreationTimestamp());
    }
}