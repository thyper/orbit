package com.mercadolibre.orbit.domain.service.impl;

import com.mercadolibre.orbit.domain.model.Planet;
import com.mercadolibre.orbit.domain.model.PlanetStatus;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.model.Weather;
import com.mercadolibre.orbit.domain.model.geometry.Point;
import com.mercadolibre.orbit.domain.model.geometry.Triangle;
import com.mercadolibre.orbit.domain.repository.PlanetStatusRepository;
import com.mercadolibre.orbit.domain.service.GeometryService;
import com.mercadolibre.orbit.domain.service.OrbitCalculationService;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsPositionException;
import com.mercadolibre.orbit.domain.service.exception.OrbitCalculationServiceRuntimeException;
import com.mercadolibre.orbit.domain.service.exception.PlanetWithoutSolarSystemException;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
public class OrbitCalculationServiceImpl implements OrbitCalculationService {

    @Value("${solarsystem.planets_by_solar_system}")
    private int planetsBySolarSystem;


    @Autowired
    private GeometryService geometryService;

    @Autowired
    private PlanetStatusRepository planetStatusRepository;




    /**
     * Spin all Solar System planets
     * Calculate orbit & weather conditions; and persist new Status
     *
     * @param solarSystem
     */
    @Override
    public void spinSolarSystem(SolarSystem solarSystem) throws InsufficientPlanetsRuntimeException {

        /*
        Planets rotation
         */

        // Check planets necessary for status computation
        if(solarSystem.getPlanets().size() == planetsBySolarSystem)
            throw new InsufficientPlanetsRuntimeException(solarSystem, planetsBySolarSystem);

        // Get rotated Planets positions for Weather computation
        List<Point> planetsPositions = new ArrayList<>();

        for(Planet planet : solarSystem.getPlanets()) {
            try {
                Point point = getPlanetRotationPosition(planet, 10); // Temporary hard coded degrees
                planetsPositions.add(point);
            } catch (PlanetWithoutSolarSystemException e) {
                throw new OrbitCalculationServiceRuntimeException(e.getMessage());
            }
        }

        /*
        Weather conditions
         */
        Weather weather = null;
        try {
            weather = getWeatherCondition(planetsPositions);
        } catch (InsufficientPlanetsPositionException e) {
            throw new OrbitCalculationServiceRuntimeException("Insufficient Planets positions for Weather calculation");
        }


    }





    /**
     * Calculate Weather conditions for Solar System
     *
     * @param planetsPositions
     * @return
     * @throws InsufficientPlanetsRuntimeException
     */
    @Override
    public Weather getWeatherCondition(List<Point> planetsPositions) throws InsufficientPlanetsPositionException {

        // Check planets necessary for status computation
        if(planetsPositions.size() == planetsBySolarSystem)
            throw new InsufficientPlanetsPositionException(planetsBySolarSystem, planetsPositions.size());

        // Calculate Weather
        Weather weather = new Weather();


        return weather;
    }






    /**
     * Calculate new planet Position
     * NO INVASIVE PROCEDURE
     *
     * @param planet
     * @param degrees
     * @return
     * @throws PlanetWithoutSolarSystemException
     */
    @Override
    public Point getPlanetRotationPosition(Planet planet, double degrees) throws PlanetWithoutSolarSystemException {

        // Get the Solar System gravity center
        SolarSystem solarSystem = planet.getSolarSystem();

        if(solarSystem == null)
            throw new PlanetWithoutSolarSystemException(planet);

        Point gravityCenter = new Point(solarSystem.getPosX(), solarSystem.getPosY());

        // Rotate planet using Planet Status last position & Solar System gravity center
        PlanetStatus planetStatus = planetStatusRepository.getLastStatus(planet.getId());
        Point startedPlanetPosition = new Point(planetStatus.getPositionX(), planetStatus.getPositionY());

        // Return new rotated Planet Position
        return geometryService.rotate(startedPlanetPosition, gravityCenter, degrees);
    }




    /**
     * Planets perimeter by last Position in database
     *
     * @param p1
     * @param p2
     * @param p3
     * @return
     */
    @Override
    public double getPlanetsPerimeter(Planet p1, Planet p2, Planet p3) {
        return getPlanetsPerimeter(
                planetStatusRepository.getLastStatus(p1.getId()),
                planetStatusRepository.getLastStatus(p2.getId()),
                planetStatusRepository.getLastStatus(p3.getId())
        );
    }


    /**
     * Calculate perimeter with Planet Status positions
     *
     * @param p1
     * @param p2
     * @param p3
     * @return
     */
    @Override
    public double getPlanetsPerimeter(PlanetStatus p1, PlanetStatus p2, PlanetStatus p3) {

        return geometryService.getTrianglePerimeter(new Triangle(
                new Point(p1.getPositionX(), p1.getPositionY()),
                new Point(p2.getPositionX(), p2.getPositionY()),
                new Point(p3.getPositionX(), p3.getPositionY())
        ));
    }


    /**
     * Check if all planets positions in a given list are aligned
     *
     * @param planetsPositions
     * @return
     * @throws InsufficientPlanetsRuntimeException
     */
    @Override
    public boolean areAligned(List<Point> planetsPositions) throws InsufficientPlanetsPositionException {

        if(planetsPositions.size() < 3)
            throw new InsufficientPlanetsPositionException(3, planetsPositions.size());

        boolean aligned = true;

        for(int i = 2; i < planetsPositions.size(); i++) {

            Point p1 = planetsPositions.get(i);
            Point p2 = planetsPositions.get(i -1);
            Point p3 = planetsPositions.get(i - 2);

            aligned &= geometryService.areCollinear(p1, p2, p3);
        }

        return aligned;
    }

    @Override
    public double getPlanetRotationDegrees(Planet planet, int days) {
        return planet.getDegreesPerDay() * days;
    }
}
