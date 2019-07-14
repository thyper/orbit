package com.mercadolibre.orbit.domain.service.exception;

public class InsufficientPlanetsPositionException extends Exception {

    public InsufficientPlanetsPositionException(int planetsNeeded, int planetsGiven) {
        super(String.format("%d planets neded, %d were given"));
    }

}
