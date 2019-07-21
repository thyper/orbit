package com.mercadolibre.orbit.domain.service.impl;

import com.mercadolibre.orbit.domain.model.Planet;
import com.mercadolibre.orbit.domain.model.PlanetStatus;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.repository.PlanetRepository;
import com.mercadolibre.orbit.domain.service.PlanetService;
import com.mercadolibre.orbit.domain.service.PlanetStatusService;
import com.mercadolibre.orbit.domain.service.exception.SolarSystemNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanetServiceImpl implements PlanetService {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private PlanetStatusService planetStatusService;


    @Override
    public Planet createPlanet(Planet planet) {

        Planet p = planetRepository.save(planet);

        // Create initial Planet Status
        PlanetStatus planetStatus = new PlanetStatus();
        planetStatus.setPlanet(p);
        planetStatus.setPositionX(planet.getSunDistance());
        planetStatus.setPositionY(0D);

        planetStatusService.create(planetStatus);

        return p;
    }

    @Override
    public Planet save(Planet planet) {
        return planetRepository.save(planet);
    }

    @Override
    public Planet findPlanetById(Long id) {
        return planetRepository.findById(id).orElse(null);
    }

    @Override
    public int countPlanetsBySolarSystem(Long solarSystemId) throws SolarSystemNotFound {
        return planetRepository.countBySolarSystem(solarSystemId);
    }

    @Override
    public List<Planet> getFromSolarSystem(SolarSystem solarSystem) throws SolarSystemNotFound {
        return planetRepository.getFromSolarSystem(solarSystem.getId());
    }
}
