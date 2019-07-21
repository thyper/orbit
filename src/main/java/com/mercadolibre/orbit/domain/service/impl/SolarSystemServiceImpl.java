package com.mercadolibre.orbit.domain.service.impl;


import com.mercadolibre.orbit.domain.enums.SolarSystemStatus;
import com.mercadolibre.orbit.domain.model.Planet;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.repository.SolarSystemRepository;
import com.mercadolibre.orbit.domain.service.PlanetService;
import com.mercadolibre.orbit.domain.service.SolarSystemService;
import com.mercadolibre.orbit.domain.service.exception.SolarSystemNotFound;
import org.springframework.beans.factory.annotation.Autowired;
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

        if(solarSystem.getRotatedToDate() == null)
            solarSystem.setRotatedToDate(new Date());

        if(solarSystem.getLastDateRotated() == null)
            solarSystem.setLastDateRotated(new Date());

        if(solarSystem.getStatus() == null)
            solarSystem.setStatus(SolarSystemStatus.OK);

        return solarSystemRepository.save(solarSystem);
    }

    @Override
    public SolarSystem findById(Long id) {
        return solarSystemRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        solarSystemRepository.deleteById(id);
    }

    @Override
    public SolarSystem save(SolarSystem solarSystem) {
        return solarSystemRepository.save(solarSystem);
    }

    @Override
    public int countPlanets(SolarSystem solarSystem) throws SolarSystemNotFound {
        return planetService.countPlanetsBySolarSystem(solarSystem.getId());
    }

    @Override
    public List<SolarSystem> getAll() {
        return solarSystemRepository.findAll();
    }

}
