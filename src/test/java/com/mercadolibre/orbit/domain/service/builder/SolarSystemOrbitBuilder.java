package com.mercadolibre.orbit.domain.service.builder;


import com.mercadolibre.orbit.domain.model.jpa.Planet;
import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;

import java.util.List;

public interface SolarSystemOrbitBuilder {

    void reset();
    SolarSystemOrbitBuilder setSolarSystem();
    SolarSystemOrbitBuilder setPlanets();
    SolarSystemOrbitBuilder setPlanetsStatuses();

    SolarSystem getSolarSystem();
    List<Planet> getPlanets();
    List<PlanetStatus> getPlanetStatuses();
}
