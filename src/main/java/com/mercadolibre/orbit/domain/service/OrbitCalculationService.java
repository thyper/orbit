package com.mercadolibre.orbit.domain.service;


import com.mercadolibre.orbit.domain.model.Planet;
import com.mercadolibre.orbit.domain.model.PlanetStatus;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.model.Weather;
import com.mercadolibre.orbit.domain.model.geometry.Point;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsPositionException;
import com.mercadolibre.orbit.domain.service.exception.PlanetWithoutSolarSystemException;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsRuntimeException;

import java.util.List;

public interface OrbitCalculationService {

    void spinSolarSystem(SolarSystem solarSystem) throws InsufficientPlanetsRuntimeException;

    Weather getWeatherCondition(List<Point> planetsPositions) throws InsufficientPlanetsPositionException;

    Point getPlanetRotationPosition(Planet planet, double degrees) throws PlanetWithoutSolarSystemException;
    double getPlanetsPerimeter(PlanetStatus p1, PlanetStatus p2, PlanetStatus p3);
    double getPlanetsPerimeter(Planet p1, Planet p2, Planet p3);

    boolean areAligned(List<Point> planetsPositions) throws InsufficientPlanetsPositionException;
    double getPlanetRotationDegrees(Planet planet, int days);
}
