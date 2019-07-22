package com.mercadolibre.orbit.domain.service;

import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;
import com.mercadolibre.orbit.domain.model.transients.WeatherQuantity;
import com.mercadolibre.orbit.domain.service.exception.ResourceNotFoundException;

import java.util.Date;
import java.util.List;

public interface SolarSystemService {

    SolarSystem createSolarSystem(SolarSystem solarSystem);
    SolarSystem findById(Long id) throws ResourceNotFoundException;
    SolarSystem save(SolarSystem solarSystem) throws ResourceNotFoundException;
    void deleteById(Long id);
    boolean existsById(Long id);

    int countPlanets(SolarSystem solarSystem) throws ResourceNotFoundException;
    List<SolarSystem> getAll();

    List<PlanetStatus> getSolarSystemStatus(SolarSystem solarSystem, Date date) throws ResourceNotFoundException;

    List<WeatherQuantity> fetchWeatherPronosticsSinceDate(Date date);
}
