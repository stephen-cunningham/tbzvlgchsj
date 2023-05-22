package com.gen.weather.models;

import com.gen.weather.entitites.MetricStatistic;
import com.gen.weather.entitites.MetricType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record QueryResponse(List<UUID> ids, LocalDateTime timeFrom, LocalDateTime timeTo, MetricStatistic metricStatistic, MetricType metricType, Double result) {
}
