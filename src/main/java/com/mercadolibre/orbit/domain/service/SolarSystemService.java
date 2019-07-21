package com.mercadolibre.orbit.domain.service;

import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.service.exception.ResourceNotFoundException;

import java.util.List;

public interface SolarSystemService {

    SolarSystem createSolarSystem(SolarSystem solarSystem);
    SolarSystem findById(Long id) throws ResourceNotFoundException;
    SolarSystem save(SolarSystem solarSystem) throws ResourceNotFoundException;
    void deleteById(Long id);
    boolean existsById(Long id);

    int countPlanets(SolarSystem solarSystem) throws ResourceNotFoundException;
    List<SolarSystem> getAll();

}
