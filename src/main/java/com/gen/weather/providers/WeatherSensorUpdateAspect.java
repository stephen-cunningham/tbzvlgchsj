package com.gen.weather.providers;

import com.gen.weather.entitites.WeatherDataPoint;
import com.gen.weather.services.WeatherSensorService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class WeatherSensorUpdateAspect {
    private final WeatherSensorService weatherSensorService;

    public WeatherSensorUpdateAspect(WeatherSensorService weatherSensorService) {
        this.weatherSensorService = weatherSensorService;
    }

    @After("execution (* com.gen.weather.repositories.WeatherDataPointRepository.save(..))")
    public void updateWeatherSensorLatestUpdate(JoinPoint joinPoint) {
        WeatherDataPoint weatherDataPoint = (WeatherDataPoint) joinPoint.getArgs()[0];

        weatherSensorService.updateLastDataCreationTimestamp(weatherDataPoint.getWeatherSensorId(), weatherDataPoint.getMetricTimestamp());
    }
}
