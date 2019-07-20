package com.mercadolibre.orbit.domain.service;


import com.mercadolibre.orbit.domain.model.Planet;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.service.exception.SolarSystemNotFound;

import java.util.List;

public interface PlanetService {

    Planet createPlanet(Planet planet);
    Planet findPlanetById(Long id);

    int countPlanetsBySolarSystem(Long solarSystemId) throws SolarSystemNotFound;

    List<Planet> getFromSolarSystem(SolarSystem solarSystem) throws SolarSystemNotFound;

}
