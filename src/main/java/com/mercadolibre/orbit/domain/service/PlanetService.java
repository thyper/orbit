package com.mercadolibre.orbit.domain.service;


import com.mercadolibre.orbit.domain.model.Planet;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.service.exception.ResourceNotFoundException;

import java.util.List;

public interface PlanetService {

    Planet createPlanet(Planet planet);
    Planet save(Planet planet) throws ResourceNotFoundException;
    Planet findPlanetById(Long id) throws ResourceNotFoundException;
    void deleteById(Long id);
    boolean existsById(Long id);

    int countPlanetsBySolarSystem(Long solarSystemId) throws ResourceNotFoundException;
    List<Planet> getFromSolarSystem(SolarSystem solarSystem) throws ResourceNotFoundException;

}
