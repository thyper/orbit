package com.mercadolibre.orbit.domain.service.exception;

public class PlanetNotFoundException extends Exception {

    public PlanetNotFoundException(String msg) {
        super(msg);
    }

    public PlanetNotFoundException(Long id) {
        super(String.format("The planet with ID-%s wasn't found", id));
    }

}
