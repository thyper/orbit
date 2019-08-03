package com.mercadolibre.orbit.domain.service.exception;

public class AmountOfPlanetsStatusException extends Exception {

    public AmountOfPlanetsStatusException(String purpose, int planetsNeeded, int planetsGiven) {
        super(String.format(
                "%d planets status needed for %s, %d were given",
                planetsNeeded, purpose, planetsGiven
        ));
    }

}
