package com.mercadolibre.orbit.domain.service;

import com.mercadolibre.orbit.domain.model.Planet;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.service.exception.SolarSystemNotFound;

import java.util.List;

public interface SolarSystemService {

    SolarSystem createSolarSystem(SolarSystem solarSystem);
    SolarSystem findById(Long id);
    void deleteById(Long id);

    int countPlanets(SolarSystem solarSystem) throws SolarSystemNotFound;

    List<SolarSystem> getAll();

}
