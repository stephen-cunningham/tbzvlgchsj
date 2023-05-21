package com.gen.weather.providers;

import com.gen.weather.entitites.WeatherDataPoint;
import com.gen.weather.entitites.WeatherSensor;
import com.gen.weather.models.WeatherSensorTimestampUpdateDTO;
import com.gen.weather.repositories.WeatherSensorRepository;
import com.gen.weather.services.WeatherSensorService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

import java.util.Optional;

@Aspect
public class WeatherSensorUpdateAspect {
    private final WeatherSensorRepository weatherSensorRepository;
    private final WeatherSensorService weatherSensorService;

    public WeatherSensorUpdateAspect(WeatherSensorRepository weatherSensorRepository, WeatherSensorService weatherSensorService) {
        this.weatherSensorRepository = weatherSensorRepository;
        this.weatherSensorService = weatherSensorService;
    }

    @After("execution (* com.gen.weather.repositories.WeatherDataPointRepository.save(..))")
    public void updateWeatherSensorLatestUpdate(JoinPoint joinPoint) {
        WeatherDataPoint weatherDataPoint = (WeatherDataPoint) joinPoint.getArgs()[0];

        WeatherSensorTimestampUpdateDTO weatherSensorTimestampUpdateDTO = new WeatherSensorTimestampUpdateDTO(
                weatherDataPoint.getWeatherSensorId(), weatherDataPoint.getMetricTimestamp());

        weatherSensorService.updateLastDataCreationTimestamp(weatherDataPoint.getWeatherSensorId(), weatherDataPoint.getMetricTimestamp());
    }
}
