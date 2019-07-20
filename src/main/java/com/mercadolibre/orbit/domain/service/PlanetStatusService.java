package com.mercadolibre.orbit.domain.service;


import com.mercadolibre.orbit.domain.model.Planet;
import com.mercadolibre.orbit.domain.model.PlanetStatus;

public interface PlanetStatusService {

    PlanetStatus create(PlanetStatus planetStatus);
    PlanetStatus getLastPlanetStatus(Planet planet);

}
