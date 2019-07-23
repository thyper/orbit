package com.mercadolibre.orbit.domain.repository;

import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;
import com.mercadolibre.orbit.domain.model.transients.WeatherQuantity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SolarSystemRepository extends JpaRepository<SolarSystem, Long> {


    @Query("SELECT ps " +
            "FROM solar_systems ss " +

            "INNER JOIN planets p ON p.solarSystem.id = ss.id " +
            "INNER JOIN planets_status ps ON ps.planet.id = p.id " +

            "WHERE ss.id = :solar_system_id " +
            "AND ps.date = :date ")
    List<PlanetStatus> fetchSolarSystemStatus(@Param("solar_system_id") Long solarSystemId,
                                              @Param("date") Date date,
                                              Pageable pageable);


    @Query("SELECT new com.mercadolibre.orbit.domain.model.transients.WeatherQuantity(ps.weatherStatus, count(ps.weatherStatus)) " +
            "FROM planets_status ps " +
            "WHERE ps.date >= :date " +
            "GROUP BY ps.weatherStatus  ")
    List<WeatherQuantity> getWeatherPronostics(@Param("date") Date date);

}
