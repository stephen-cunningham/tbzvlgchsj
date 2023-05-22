package com.gen.weather.controllers;

import com.gen.weather.entitites.WeatherSensor;
import com.gen.weather.models.WeatherSensorCreateDTO;
import com.gen.weather.models.WeatherSensorUpdateDTO;
import com.gen.weather.services.WeatherSensorService;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RepositoryRestController
public class WeatherSensorController {
    public static final String URI_BASE = "/weatherSensors";

    private final WeatherSensorService weatherSensorService;

    public WeatherSensorController(WeatherSensorService weatherSensorService) {
        this.weatherSensorService = weatherSensorService;
    }

    @PostMapping(URI_BASE)
    public ResponseEntity<WeatherSensor> createWeatherSensor(@RequestBody WeatherSensorCreateDTO weatherSensorCreateDTO) {
        WeatherSensor weatherSensor = weatherSensorService.save(weatherSensorCreateDTO.weatherStationId());
        return ResponseEntity.status(HttpStatus.CREATED).body(weatherSensor);
    }

    @PatchMapping(URI_BASE)
    public ResponseEntity<Void> updateWeatherSensorStatus(@RequestBody WeatherSensorUpdateDTO weatherSensorUpdateDTO) {
        weatherSensorService.updateStatus(weatherSensorUpdateDTO.id(), weatherSensorUpdateDTO.sensorStatus());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
