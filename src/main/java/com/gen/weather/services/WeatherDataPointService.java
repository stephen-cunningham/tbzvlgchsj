package com.gen.weather.services;

import com.gen.weather.entitites.MetricStatistic;
import com.gen.weather.entitites.MetricType;
import com.gen.weather.entitites.WeatherDataPoint;
import com.gen.weather.models.QueryResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface WeatherDataPointService {
    QueryResponse query(List<UUID> ids, LocalDateTime timeFrom, LocalDateTime timeTo, MetricStatistic metricStatistic, MetricType metricType);

    WeatherDataPoint save(UUID weatherSensorId, MetricType metricType, Double metricValue);
}
