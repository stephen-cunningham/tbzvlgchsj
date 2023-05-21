package com.gen.weather.repositories;

import com.gen.weather.entitites.WeatherSensor;
import com.gen.weather.entitites.WeatherStation;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface WeatherStationRepository extends JpaRepository<WeatherStation, UUID> {
    @Override
    @RestResource(exported = false)
    <S extends WeatherStation> S save(S weatherStation);

    @Override
    @RestResource(exported = false)
    void delete(WeatherStation weatherStation);
}