package com.mercadolibre.orbit.domain.service.impl;

import com.mercadolibre.orbit.domain.model.Planet;
import com.mercadolibre.orbit.domain.model.PlanetStatus;
import com.mercadolibre.orbit.domain.repository.PlanetStatusRepository;
import com.mercadolibre.orbit.domain.service.PlanetStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanetStatusServiceImpl implements PlanetStatusService {

    @Autowired
    private PlanetStatusRepository planetStatusRepository;


    @Override
    public PlanetStatus create(PlanetStatus planetStatus) {
        return planetStatusRepository.save(planetStatus);
    }

    @Override
    public PlanetStatus getLastPlanetStatus(Planet planet) {
        return planetStatusRepository.getLastStatus(planet.getId(), PageRequest.of(0, 1)).get(0);
    }
}
