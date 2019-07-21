package com.mercadolibre.orbit.domain.service.impl;


import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.repository.SolarSystemRepository;
import com.mercadolibre.orbit.domain.service.PlanetService;
import com.mercadolibre.orbit.domain.service.SolarSystemService;
import com.mercadolibre.orbit.domain.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class SolarSystemServiceImpl implements SolarSystemService {

    // Services
    @Autowired
    private PlanetService planetService;

    // Repositories
    @Autowired
    private SolarSystemRepository solarSystemRepository;





    @Override
    public SolarSystem createSolarSystem(SolarSystem solarSystem) {
        return solarSystemRepository.save(solarSystem);
    }

    @Override
    public SolarSystem findById(Long id) throws ResourceNotFoundException {

        if(!solarSystemRepository.existsById(id))
            throw new ResourceNotFoundException(SolarSystem.class, id);

        return solarSystemRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        solarSystemRepository.deleteById(id);
    }

    @Override
    public SolarSystem save(SolarSystem solarSystem) throws ResourceNotFoundException {

        if(!solarSystemRepository.existsById(solarSystem.getId()))
            throw new ResourceNotFoundException(SolarSystem.class, solarSystem.getId());

        return solarSystemRepository.save(solarSystem);
    }

    @Override
    public int countPlanets(SolarSystem solarSystem) throws ResourceNotFoundException {

        if(!solarSystemRepository.existsById(solarSystem.getId()))
            throw new ResourceNotFoundException(SolarSystem.class, solarSystem.getId());

        return planetService.countPlanetsBySolarSystem(solarSystem.getId());
    }

    @Override
    public boolean existsById(Long id) {
        return solarSystemRepository.existsById(id);
    }

    @Override
    public List<SolarSystem> getAll() {
        return solarSystemRepository.findAll();
    }

}
