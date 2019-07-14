package com.mercadolibre.orbit.domain.service.exception;

public class InsufficientPlanetsStatusException extends Exception {

    public InsufficientPlanetsStatusException(String purpose, int planetsNeeded, int planetsGiven) {
        super(String.format(
                "%d planets status needed for %s, %d were given",
                planetsNeeded, purpose, planetsGiven
        ));
    }

}
