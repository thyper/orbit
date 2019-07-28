package com.mercadolibre.orbit.domain.service;

import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.model.transients.Point;
import com.mercadolibre.orbit.domain.model.transients.Sphere;
import com.mercadolibre.orbit.domain.model.transients.Weather;
import com.mercadolibre.orbit.domain.model.transients.WeatherQuantity;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsPositionException;

import java.util.Date;
import java.util.List;

public interface WeatherService {

    Weather getWeatherCondition(Sphere gravityCenter, List<PlanetStatus> planetStatuses)
            throws InsufficientPlanetsPositionException;

    List<WeatherQuantity> fetchWeatherPronosticsSinceDate(Date date);

}
