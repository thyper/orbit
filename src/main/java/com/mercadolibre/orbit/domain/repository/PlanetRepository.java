package com.mercadolibre.orbit.domain.repository;

import com.mercadolibre.orbit.domain.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long> {

    @Query("SELECT COUNT(p) FROM planets p WHERE p.solarsystem.id = :id")
    Integer countBySolarSystem(@Param("id") Long solarSystemId);

    @Query("SELECT p FROM planets WHETE p.solarsystem.id = :ss_id")
    List<Planet> getFromSolarSystem(@Param("ss_id") Long solarSystemId);

}
