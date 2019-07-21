package com.mercadolibre.orbit.domain.service.impl;

import com.mercadolibre.orbit.domain.enums.WeatherStatus;
import com.mercadolibre.orbit.domain.model.Planet;
import com.mercadolibre.orbit.domain.model.PlanetStatus;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.repository.PlanetRepository;
import com.mercadolibre.orbit.domain.service.PlanetService;
import com.mercadolibre.orbit.domain.service.PlanetStatusService;
import com.mercadolibre.orbit.domain.service.SolarSystemService;
import com.mercadolibre.orbit.domain.service.exception.ResourceNotFoundException;
import com.mercadolibre.orbit.domain.service.exception.SolarSystemNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

@Service
public class PlanetServiceImpl implements PlanetService {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private PlanetStatusService planetStatusService;

    @Autowired
    private SolarSystemService solarSystemService;


    @Override
    public Planet createPlanet(Planet planet) {

        Planet p = planetRepository.save(planet);

        // Create initial Planet Status
        PlanetStatus planetStatus = new PlanetStatus();
        planetStatus.setPlanet(p);
        planetStatus.setPositionX(planet.getSunDistance());
        planetStatus.setPositionY(0D);
        planetStatus.setWeatherStatus(WeatherStatus.DROUGHT);
        planetStatus.setDate(new Date());

        planetStatusService.create(planetStatus);

        return p;
    }

    @Override
    public Planet save(Planet planet) throws ResourceNotFoundException {

        if(!planetRepository.existsById(planet.getId()))
            throw new ResourceNotFoundException(Planet.class, planet.getId());

        return planetRepository.save(planet);
    }

    @Override
    public Planet findPlanetById(Long id) throws ResourceNotFoundException {

        if(!planetRepository.existsById(id))
            throw new ResourceNotFoundException(Planet.class, id);

        return planetRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        planetRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return planetRepository.existsById(id);
    }

    @Override
    public int countPlanetsBySolarSystem(Long solarSystemId) throws ResourceNotFoundException {

        if(!solarSystemService.existsById(solarSystemId))
            throw new ResourceNotFoundException(SolarSystem.class, solarSystemId);

        return planetRepository.countBySolarSystem(solarSystemId);
    }

    @Override
    public List<Planet> getFromSolarSystem(SolarSystem solarSystem)  throws ResourceNotFoundException {

        if(!solarSystemService.existsById(solarSystem.getId()))
            throw new ResourceNotFoundException(SolarSystem.class, solarSystem.getId());

        return planetRepository.getFromSolarSystem(solarSystem.getId());
    }
}
