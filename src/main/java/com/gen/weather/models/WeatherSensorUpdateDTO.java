package com.gen.weather.models;

import com.gen.weather.entitites.SensorStatus;

import java.util.UUID;

public record WeatherSensorUpdateDTO(UUID id, SensorStatus sensorStatus) {}
