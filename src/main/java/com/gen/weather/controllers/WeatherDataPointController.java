package com.gen.weather.controllers;

import com.gen.weather.entitites.MetricStatistic;
import com.gen.weather.entitites.MetricType;
import com.gen.weather.entitites.WeatherDataPoint;
import com.gen.weather.models.QueryResponse;
import com.gen.weather.models.WeatherDataPointDTO;
import com.gen.weather.services.WeatherDataPointService;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RepositoryRestController
public class WeatherDataPointController {
    public static final String URI_BASE = "/weatherDataPoints";

    private final WeatherDataPointService weatherDataPointService;

    public WeatherDataPointController(WeatherDataPointService weatherDataPointService) {
        this.weatherDataPointService = weatherDataPointService;
    }

    @GetMapping(URI_BASE + "/search/query")
    public ResponseEntity<QueryResponse> query(
            @RequestParam(value = "ids", required = false) List<UUID> ids,
            @RequestParam(value = "timeFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeFrom,
            @RequestParam(value = "timeTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeTo,
            @RequestParam(value = "statistic") MetricStatistic metricStatistic,
            @RequestParam(value = "metricType", required = false) MetricType metricType) {
        QueryResponse queryResponse = weatherDataPointService.query(ids, timeFrom, timeTo, metricStatistic, metricType);
        return ResponseEntity.ok(queryResponse);
    }

    @PostMapping(URI_BASE)
    public ResponseEntity<WeatherDataPoint> createWeatherDataPoint(@RequestBody WeatherDataPointDTO weatherDataPointDTO) {
        WeatherDataPoint weatherDataPoint = weatherDataPointService.save(
                weatherDataPointDTO.weatherSensorId(), weatherDataPointDTO.metricType(), weatherDataPointDTO.metricValue());

        return ResponseEntity.status(HttpStatus.CREATED).body(weatherDataPoint);
    }
}
