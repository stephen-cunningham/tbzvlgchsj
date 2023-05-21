package com.gen.weather.models;

import java.time.LocalDateTime;
import java.util.UUID;

public record WeatherSensorTimestampUpdateDTO(UUID id, LocalDateTime lastDataCreationTimestamp) {}
