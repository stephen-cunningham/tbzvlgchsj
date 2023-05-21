package com.gen.weather.repositories;

import com.gen.weather.entitites.WeatherDataPoint;
import com.gen.weather.entitites.WeatherSensor;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource
public interface WeatherSensorRepository extends JpaRepository<WeatherSensor, UUID> {
    @Override
    @RestResource(exported = false)
    <S extends WeatherSensor> S save(S weatherSensor);

    @Override
    @RestResource(exported = false)
    void delete(WeatherSensor weatherSensor);
}
