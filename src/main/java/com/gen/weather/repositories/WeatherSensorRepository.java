package com.gen.weather.repositories;

import com.gen.weather.entitites.WeatherSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.UUID;

@RepositoryRestResource
public interface WeatherSensorRepository extends JpaRepository<WeatherSensor, UUID> {
    @Override
    @RestResource(exported = false)
    <S extends WeatherSensor> S save(S weatherSensor);

    @Override
    @RestResource(exported = false)
    void delete(WeatherSensor weatherSensor);
}
