package com.gen.weather.controllers;

import com.gen.weather.models.WeatherDataPointDTO;
import com.gen.weather.services.WeatherDataPointService;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RepositoryRestController
public class WeatherDataPointController {
    public static final String URI_BASE = "/weatherDataPoints";

    private final WeatherDataPointService weatherDataPointService;

    public WeatherDataPointController(WeatherDataPointService weatherDataPointService) {
        this.weatherDataPointService = weatherDataPointService;
    }

    @PostMapping(URI_BASE)
    public ResponseEntity<Void> createWeatherDataPoint(@RequestBody WeatherDataPointDTO weatherDataPointDTO) {
        weatherDataPointService.save(weatherDataPointDTO.weatherSensorId(), weatherDataPointDTO.metricType(), weatherDataPointDTO.metricValue());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
