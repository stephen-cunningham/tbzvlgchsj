package com.gen.weather.repositories;

import com.gen.weather.entitites.MetricType;
import com.gen.weather.entitites.WeatherDataPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RepositoryRestResource
public interface WeatherDataPointRepository extends JpaRepository<WeatherDataPoint, UUID> {
    @Override
    @RestResource(exported = false)
    <S extends WeatherDataPoint> S save(S weatherDataPoint);

    @RestResource(exported = false)
    @Query("select wdp.metricValue from #{#entityName} wdp " +
            "where (:weatherSensorId = null or wdp.weatherSensorId = :weatherSensorId) and " +
            "(:metricType = null or wdp.metricType = :metricType) and " +
            "(:metricTimestampFrom = null or wdp.metricTimestamp >= :metricTimestampFrom) and " +
            "(:metricTimestampTo = null or wdp.metricTimestamp <= :metricTimestampTo)")
    List<Double> filterMetricValuesByParams(
            UUID weatherSensorId,
            MetricType metricType,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime metricTimestampFrom,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime metricTimestampTo);

    @Query("select wdp from #{#entityName} wdp " +
            "where wdp.weatherSensorId = :weatherSensorId " +
            "order by wdp.metricTimestamp desc limit 1")
    WeatherDataPoint findMostRecentByWeatherSensorId(UUID weatherSensorId);
}
