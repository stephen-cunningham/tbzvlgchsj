package com.gen.weather.services;

import com.gen.weather.entitites.MetricType;
import com.gen.weather.entitites.WeatherDataPoint;
import com.gen.weather.models.WeatherDataPointDTO;

import java.util.UUID;

public interface WeatherDataPointService {
    WeatherDataPoint save(UUID weatherSensorId, MetricType metricType, Double metricValue);
}
