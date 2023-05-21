package com.gen.weather.services;

import com.gen.weather.entitites.MetricType;
import com.gen.weather.entitites.WeatherDataPoint;
import com.gen.weather.repositories.WeatherDataPointRepository;
import com.gen.weather.repositories.WeatherSensorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class WeatherDataPointServiceImpl implements WeatherDataPointService {
    private final WeatherDataPointRepository weatherDataPointRepository;
    private final WeatherSensorRepository weatherSensorRepository;

    public WeatherDataPointServiceImpl(WeatherDataPointRepository weatherDataPointRepository, WeatherSensorRepository weatherSensorRepository) {
        this.weatherDataPointRepository = weatherDataPointRepository;
        this.weatherSensorRepository = weatherSensorRepository;
    }

    @Override
    @Transactional
    public WeatherDataPoint save(UUID weatherSensorId, MetricType metricType, Double metricValue) {
        if (weatherSensorRepository.findById(weatherSensorId).isEmpty()) {
            throw new IllegalArgumentException(String.format("Weather Sensor does not exist with id %s", weatherSensorId));
        }

        WeatherDataPoint weatherDataPoint = new WeatherDataPoint(weatherSensorId, metricType, metricValue);

        return weatherDataPointRepository.save(weatherDataPoint);
    }
}
