package com.mercadolibre.orbit.domain.service.impl;

import com.mercadolibre.orbit.domain.enums.WeatherStatus;
import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.model.transients.Point;
import com.mercadolibre.orbit.domain.model.transients.Triangle;
import com.mercadolibre.orbit.domain.model.transients.Weather;
import com.mercadolibre.orbit.domain.model.transients.WeatherQuantity;
import com.mercadolibre.orbit.domain.repository.WeatherRepository;
import com.mercadolibre.orbit.domain.service.OrbitCalculationService;
import com.mercadolibre.orbit.domain.service.WeatherService;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsException;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsPositionException;
import com.mercadolibre.orbit.domain.service.util.GeometryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class WeatherServiceImpl implements WeatherService {

    private final int planetsBySolarSystem = 3;

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private OrbitCalculationService orbitCalculationService;





    /**
     * Calculate Weather conditions for Solar System
     *
     * @param gravityCenter
     * @param planetStatuses
     * @return Weather
     * @throws InsufficientPlanetsException
     */
    @Override
    public Weather getWeatherCondition(Point gravityCenter, List<PlanetStatus> planetStatuses)
            throws InsufficientPlanetsPositionException {

        // Check planets necessary for status computation
        if(planetStatuses.size() != planetsBySolarSystem)
            throw new InsufficientPlanetsPositionException(planetsBySolarSystem, planetStatuses.size());

        // Calculate Weather
        Weather weather = new Weather();


        // Create Points positions of Planets
        // Points are rounded so the calculation dont fail because the double accuracy
        List<Point> planetsPositions = new ArrayList<>();
        planetsPositions.add(new Point(planetStatuses.get(0).getPositionX(),
                planetStatuses.get(0).getPositionY()));
        planetsPositions.add(new Point(planetStatuses.get(1).getPositionX(),
                planetStatuses.get(1).getPositionY()));
        planetsPositions.add(new Point(planetStatuses.get(2).getPositionX(),
                planetStatuses.get(2).getPositionY()));


        if(orbitCalculationService.areAligned(planetsPositions)) {
            // Add sun
            planetsPositions.add(gravityCenter);

            // Check alignment with gravity center
            if(orbitCalculationService.areAligned(planetsPositions)) {
                // Alignment with sun
                weather.setWeatherStatus(WeatherStatus.DROUGHT);
            }else {
                // Alignment without sun
                weather.setWeatherStatus(WeatherStatus.OPTIMAL);
            }
        }else {
            // Create planets Triangle
            Triangle planetsTriangle = new Triangle(
                    planetsPositions.get(0),
                    planetsPositions.get(1),
                    planetsPositions.get(2)
            );

            // Call GeometryService to check if gravity center is inside Triangle
            if(GeometryUtils.detectCollision(planetsTriangle, gravityCenter)) {
                // If sun is inside Triangle
                double perimeter = orbitCalculationService.getPlanetsPerimeter(planetStatuses.get(0),
                        planetStatuses.get(1),
                        planetStatuses.get(2));

                weather.setWeatherStatus(WeatherStatus.RAINFALL);
                weather.setIntensity(perimeter);
            }else {
                // If sun is outside Triangle
                weather.setWeatherStatus(WeatherStatus.UNKNOWN);
            }
        }


        return weather;
    }

    @Override
    public List<WeatherQuantity> fetchWeatherPronosticsSinceDate(Date date) {
        return weatherRepository.fetchWeatherPronosticsSinceDate(date);
    }

}
