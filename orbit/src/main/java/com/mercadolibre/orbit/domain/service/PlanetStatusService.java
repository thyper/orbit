package com.mercadolibre.orbit.domain.service;


import com.mercadolibre.orbit.domain.model.jpa.Planet;
import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.service.exception.ResourceNotFoundException;

public interface PlanetStatusService {

    PlanetStatus create(PlanetStatus planetStatus);
    PlanetStatus save(PlanetStatus planetStatus) throws ResourceNotFoundException;
    PlanetStatus findById(Long id) throws ResourceNotFoundException;
    boolean existsById(Long id);
    void deleteById(Long id);

    PlanetStatus getLastPlanetStatus(Planet planet) throws ResourceNotFoundException;

}
