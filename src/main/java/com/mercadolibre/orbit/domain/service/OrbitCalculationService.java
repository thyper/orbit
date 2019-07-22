package com.mercadolibre.orbit.domain.service;


import com.mercadolibre.orbit.domain.enums.SpiningStatus;
import com.mercadolibre.orbit.domain.model.jpa.Planet;
import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.model.transients.Weather;
import com.mercadolibre.orbit.domain.model.transients.Point;
import com.mercadolibre.orbit.domain.model.transients.WeatherQuantity;
import com.mercadolibre.orbit.domain.service.exception.*;

import java.util.Date;
import java.util.List;

public interface OrbitCalculationService {

    SpiningStatus spinSolarSystems(Date toDate);

    Weather getWeatherCondition(Point gravityCenter, List<PlanetStatus> planetStatuses) throws InsufficientPlanetsPositionException;

    Point getPlanetRotationPosition(Planet planet, double degrees) throws PlanetWithoutSolarSystemException, PlanetNotFoundException;
    double getPlanetsPerimeter(PlanetStatus p1, PlanetStatus p2, PlanetStatus p3);
    double getPlanetsPerimeter(Planet p1, Planet p2, Planet p3) throws ResourceNotFoundException;

    boolean areAligned(List<Point> planetsPositions) throws InsufficientPlanetsPositionException;
    double getPlanetRotationDegrees(Planet planet, int days);
}
