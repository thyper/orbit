package com.mercadolibre.orbit.domain.repository;

import com.mercadolibre.orbit.domain.model.SolarSystem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolarSystemRepository extends JpaRepository<SolarSystem, Long> {
}
