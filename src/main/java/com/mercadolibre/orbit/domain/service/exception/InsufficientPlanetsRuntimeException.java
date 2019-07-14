package com.mercadolibre.orbit.domain.service.exception;

import com.mercadolibre.orbit.domain.model.SolarSystem;

public class InsufficientPlanetsRuntimeException extends RuntimeException {

    public InsufficientPlanetsRuntimeException(String purpose, int planetsNeeded, int planetsGiven) {
        super(String.format(
                "%d planets needed for %s, %d were given",
                planetsNeeded, purpose, planetsGiven
        ));
    }

    public InsufficientPlanetsRuntimeException(SolarSystem solarSystem, int planetsExpected) {
        super(String.format(
                "%d planets necesarry for Solar System, '%d' were given",
                solarSystem.getId(), planetsExpected));
    }

}
