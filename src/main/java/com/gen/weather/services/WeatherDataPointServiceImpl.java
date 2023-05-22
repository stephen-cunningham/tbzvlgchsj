package com.gen.weather.services;

import com.gen.weather.entitites.MetricStatistic;
import com.gen.weather.entitites.MetricType;
import com.gen.weather.entitites.WeatherDataPoint;
import com.gen.weather.exceptions.NotFoundException;
import com.gen.weather.models.QueryResponse;
import com.gen.weather.providers.aggregation.*;
import com.gen.weather.repositories.WeatherDataPointRepository;
import com.gen.weather.repositories.WeatherSensorRepository;
import com.gen.weather.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class WeatherDataPointServiceImpl implements WeatherDataPointService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherDataPointServiceImpl.class);

    private final WeatherDataPointRepository weatherDataPointRepository;
    private final WeatherSensorRepository weatherSensorRepository;

    public WeatherDataPointServiceImpl(WeatherDataPointRepository weatherDataPointRepository, WeatherSensorRepository weatherSensorRepository) {
        this.weatherDataPointRepository = weatherDataPointRepository;
        this.weatherSensorRepository = weatherSensorRepository;
    }

    @Override
    public QueryResponse query(List<UUID> weatherSensorIds, LocalDateTime timeFrom, LocalDateTime timeTo, MetricStatistic metricStatistic, MetricType metricType) {
        LocalDateTime[] times = TimeUtils.getTimeFromAndTimeTo(timeFrom, timeTo);
        timeFrom = times[0];
        timeTo = times[1];

        LOGGER.info(String.format("Filtering Weather Data Points with timeFrom %s and timeTo %s", timeFrom, timeTo));
        List<Double> weatherDataPointMetricValues = getWeatherDataPointMetricValues(weatherSensorIds, timeFrom, timeTo, metricType);

        if (weatherDataPointMetricValues.size() == 0) {
            String message = String.format(
                    "Weather Data Points not found for the %s metric for the following weather sensors: %s", metricType, weatherSensorIds);
            throw new NotFoundException(message);
        }

        Double result = aggregate(metricStatistic, weatherDataPointMetricValues);

        return new QueryResponse(weatherSensorIds, timeFrom, timeTo, metricStatistic, metricType, result);
    }

    private List<Double> getWeatherDataPointMetricValues(List<UUID> weatherSensorIds, LocalDateTime timeFrom, LocalDateTime timeTo, MetricType metricType) {
        if (weatherSensorIds == null) {
            return weatherDataPointRepository.filterMetricValuesByParams(null, metricType, timeFrom, timeTo);
        }

        List<Double> weatherDataPointMetricValues = new ArrayList<>();
        for (UUID weatherSensorId : weatherSensorIds) {
            List<Double> weatherDataPointsForId = weatherDataPointRepository.filterMetricValuesByParams(
                    weatherSensorId, metricType, timeFrom, timeTo);
            weatherDataPointMetricValues.addAll(weatherDataPointsForId);
        }

        return weatherDataPointMetricValues;
    }

    private static Double aggregate(MetricStatistic metricStatistic, List<Double> weatherDataPointMetricValues) {
        MetricAggregationStrategy metricAggregationStrategy = switch (metricStatistic) {
            case MAX -> new MaxMetricAggregationStrategy();
            case MIN -> new MinMetricAggregationStrategy();
            case AVERAGE -> new AverageMetricAggregationStrategy();
            case SUM -> new SumMetricAggregationStrategy();
        };

        return metricAggregationStrategy.aggregate(weatherDataPointMetricValues);
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
