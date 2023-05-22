package com.gen.weather.services;

import com.gen.weather.entitites.SensorStatus;
import com.gen.weather.entitites.WeatherSensor;
import com.gen.weather.repositories.WeatherSensorRepository;
import com.gen.weather.repositories.WeatherStationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class WeatherSensorServiceImpl implements WeatherSensorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherSensorServiceImpl.class);
    private final WeatherSensorRepository weatherSensorRepository;
    private final WeatherStationRepository weatherStationRepository;

    public WeatherSensorServiceImpl(WeatherSensorRepository weatherSensorRepository, WeatherStationRepository weatherStationRepository) {
        this.weatherSensorRepository = weatherSensorRepository;
        this.weatherStationRepository = weatherStationRepository;
    }

    @Override
    @Transactional
    public WeatherSensor save(UUID weatherStationId) {
        if (weatherStationRepository.findById(weatherStationId).isEmpty()) {
            throw new IllegalArgumentException(String.format("Weather Station does not exist with id %s", weatherStationId));
        }

        WeatherSensor weatherSensor = new WeatherSensor();
        weatherSensor.setWeatherStationId(weatherStationId);

        return weatherSensorRepository.save(weatherSensor);
    }

    @Override
    @Transactional
    public void updateStatus(UUID id, SensorStatus sensorStatus) {
        Optional<WeatherSensor> weatherSensorOptional = weatherSensorRepository.findById(id);
        if (weatherSensorOptional.isEmpty()) {
            throw new IllegalArgumentException(String.format("Weather Sensor does not exist with id %s", id));
        }

        WeatherSensor weatherSensor = weatherSensorOptional.get();
        weatherSensor.setSensorStatus(sensorStatus);

        weatherSensorRepository.save(weatherSensor);
    }

    @Override
    @Transactional
    public void updateLastDataCreationTimestamp(UUID id, LocalDateTime timestamp) {
        Optional<WeatherSensor> weatherSensorOptional = weatherSensorRepository.findById(id);

        if (weatherSensorOptional.isPresent()) {
            LOGGER.info(String.format("Updating WeatherSensor %s. Setting lastDataCreationTimestamp to %s", id, timestamp));
            WeatherSensor weatherSensor = weatherSensorOptional.get();
            weatherSensor.setLastDataCreationTimestamp(timestamp);
            weatherSensorRepository.save(weatherSensor);
        }
    }
}
