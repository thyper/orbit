package com.mercadolibre.orbit.domain.service.impl;


import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;
import com.mercadolibre.orbit.domain.model.transients.WeatherQuantity;
import com.mercadolibre.orbit.domain.repository.SolarSystemRepository;
import com.mercadolibre.orbit.domain.service.PlanetService;
import com.mercadolibre.orbit.domain.service.SolarSystemService;
import com.mercadolibre.orbit.domain.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public long countSolarSystems() {
        return solarSystemRepository.count();
    }

    @Override
    public boolean existsById(Long id) {
        return solarSystemRepository.existsById(id);
    }

    @Override
    public List<SolarSystem> getAll() {
        return solarSystemRepository.findAll();
    }

    @Override
    public List<PlanetStatus> getSolarSystemStatus(SolarSystem solarSystem, Date date) throws ResourceNotFoundException {

        int nPlanets = this.countPlanets(solarSystem);

        return solarSystemRepository.fetchSolarSystemStatus(solarSystem.getId(),
                date,
                PageRequest.of(0, nPlanets));
    }

}
