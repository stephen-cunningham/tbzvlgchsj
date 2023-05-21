package com.gen.weather.models;

import com.gen.weather.entitites.MetricType;

import java.util.UUID;

public record WeatherDataPointDTO(UUID weatherSensorId, MetricType metricType, Double metricValue) {}
