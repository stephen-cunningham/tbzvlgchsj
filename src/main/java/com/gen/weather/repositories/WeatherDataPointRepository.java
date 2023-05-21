package com.gen.weather.repositories;

import com.gen.weather.entitites.WeatherDataPoint;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource
public interface WeatherDataPointRepository extends JpaRepository<WeatherDataPoint, UUID> {
    @Override
    @RestResource(exported = false)
    <S extends WeatherDataPoint> S save(S weatherDataPoint);
}
