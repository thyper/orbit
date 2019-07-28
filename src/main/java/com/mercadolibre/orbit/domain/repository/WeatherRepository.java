package com.mercadolibre.orbit.domain.repository;

import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;
import com.mercadolibre.orbit.domain.model.transients.WeatherQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface WeatherRepository extends JpaRepository<SolarSystem, Long> {

    @Query("SELECT new com.mercadolibre.orbit.domain.model.transients.WeatherQuantity(ps.weatherStatus, count(ps.weatherStatus)) " +
            "FROM planets_status ps " +
            "WHERE ps.date >= :date " +
            "GROUP BY ps.weatherStatus  ")
    List<WeatherQuantity> fetchWeatherPronosticsSinceDate(@Param("date") Date date);

}
