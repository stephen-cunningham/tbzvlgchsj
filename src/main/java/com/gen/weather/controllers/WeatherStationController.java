package com.gen.weather.controllers;

import com.gen.weather.entitites.WeatherStation;
import com.gen.weather.models.WeatherStationDTO;
import com.gen.weather.repositories.WeatherStationRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RepositoryRestController
public class WeatherStationController {
    public static final String URI_BASE = "/weatherStations";
    private final WeatherStationRepository weatherStationRepository;

    public WeatherStationController(WeatherStationRepository weatherStationRepository) {
        this.weatherStationRepository = weatherStationRepository;
    }

    @PostMapping(URI_BASE)
    public ResponseEntity<WeatherStation> createWeatherStation(@RequestBody WeatherStationDTO weatherStationDTO) {
        WeatherStation weatherStation = weatherStationRepository.save(WeatherStation.from(weatherStationDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(weatherStation);
    }
}
