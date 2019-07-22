package com.mercadolibre.orbit.domain.service.exception;

import com.mercadolibre.orbit.domain.model.jpa.Planet;

public class PlanetWithoutSolarSystemException extends Exception {

    public PlanetWithoutSolarSystemException(Planet planet) {
        super(String.format("Planet '%d' need a Solar System", planet.getId()));
    }

}
