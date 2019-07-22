package com.mercadolibre.orbit.domain.service.impl;

import com.mercadolibre.orbit.domain.model.jpa.Planet;
import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.repository.PlanetStatusRepository;
import com.mercadolibre.orbit.domain.service.PlanetService;
import com.mercadolibre.orbit.domain.service.PlanetStatusService;
import com.mercadolibre.orbit.domain.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PlanetStatusServiceImpl implements PlanetStatusService {

    @Autowired
    private PlanetStatusRepository planetStatusRepository;

    @Autowired
    private PlanetService planetService;


    @Override
    public PlanetStatus create(PlanetStatus planetStatus) {
        return planetStatusRepository.save(planetStatus);
    }

    @Override
    public PlanetStatus save(PlanetStatus planetStatus) throws ResourceNotFoundException {

        if(!planetStatusRepository.existsById(planetStatus.getId()))
            throw new ResourceNotFoundException(PlanetStatus.class, planetStatus.getId());

        return planetStatusRepository.save(planetStatus);
    }

    @Override
    public PlanetStatus findById(Long id) throws ResourceNotFoundException {

        if(!planetStatusRepository.existsById(id))
            throw new ResourceNotFoundException(PlanetStatus.class, id);

        return planetStatusRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsById(Long id) {
        return planetStatusRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        planetStatusRepository.deleteById(id);
    }

    @Override
    public PlanetStatus getLastPlanetStatus(Planet planet) throws ResourceNotFoundException {

        if(!planetService.existsById(planet.getId()))
            throw new ResourceNotFoundException(Planet.class, planet.getId());

        return planetStatusRepository.getLastStatus(planet.getId(), PageRequest.of(0, 1)).get(0);
    }
}
