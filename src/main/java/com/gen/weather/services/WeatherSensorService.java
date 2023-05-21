package com.gen.weather.services;

import com.gen.weather.entitites.SensorStatus;
import com.gen.weather.entitites.WeatherSensor;
import com.gen.weather.models.WeatherSensorCreateDTO;
import com.gen.weather.models.WeatherSensorStatusUpdateDTO;
import com.gen.weather.models.WeatherSensorTimestampUpdateDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public interface WeatherSensorService {
    WeatherSensor save(UUID weatherStationId);

    void updateStatus(UUID id, SensorStatus sensorStatus);

    void updateLastDataCreationTimestamp(UUID id, LocalDateTime timestamp);
}
