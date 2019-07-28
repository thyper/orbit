package com.mercadolibre.orbit.domain.service.impl;

import com.mercadolibre.orbit.domain.enums.WeatherStatus;
import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.model.transients.*;
import com.mercadolibre.orbit.domain.repository.WeatherRepository;
import com.mercadolibre.orbit.domain.service.OrbitCalculationService;
import com.mercadolibre.orbit.domain.service.WeatherService;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsException;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsPositionException;
import com.mercadolibre.orbit.domain.service.util.GeometryUtils;
import org.mapstruct.factory.Mappers;
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
    public Weather getWeatherCondition(Sphere gravityCenter, List<PlanetStatus> planetStatuses)
            throws InsufficientPlanetsPositionException {

        // Check planets necessary for status computation
        if(planetStatuses.size() != planetsBySolarSystem)
            throw new InsufficientPlanetsPositionException(planetsBySolarSystem, planetStatuses.size());

        // Calculate Weather
        Weather weather = new Weather();


        // Create Points positions of Planets
        // Points are rounded so the calculation dont fail because the double accuracy

        List<Sphere> planetsSpheres = new ArrayList<>();
        planetsSpheres.add(new Sphere(planetStatuses.get(0).getPositionX(),
                planetStatuses.get(0).getPositionY(),
                planetStatuses.get(0).getPlanet().getRadius()));
        planetsSpheres.add(new Sphere(planetStatuses.get(1).getPositionX(),
                planetStatuses.get(1).getPositionY(),
                planetStatuses.get(1).getPlanet().getRadius()));
        planetsSpheres.add(new Sphere(planetStatuses.get(2).getPositionX(),
                planetStatuses.get(2).getPositionY(),
                planetStatuses.get(2).getPlanet().getRadius()));


        if(orbitCalculationService.areAligned(planetsSpheres)) {
            // Add sun
            planetsSpheres.add(gravityCenter);

            // Check alignment with gravity center
            if(orbitCalculationService.areAligned(planetsSpheres)) {
                // Alignment with sun
                weather.setWeatherStatus(WeatherStatus.DROUGHT);
            }else {
                // Alignment without sun
                weather.setWeatherStatus(WeatherStatus.OPTIMAL);
            }
        }else {
            // Create planets Triangle
            Triangle planetsTriangle = new Triangle(
                    planetsSpheres.get(0),
                    planetsSpheres.get(1),
                    planetsSpheres.get(2)
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
