package com.mercadolibre.orbit.domain.service.impl;

import com.mercadolibre.orbit.domain.enums.ClockDirection;
import com.mercadolibre.orbit.domain.enums.WeatherStatus;
import com.mercadolibre.orbit.domain.model.Planet;
import com.mercadolibre.orbit.domain.model.PlanetStatus;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.model.Weather;
import com.mercadolibre.orbit.domain.model.geometry.Point;
import com.mercadolibre.orbit.domain.model.geometry.Triangle;
import com.mercadolibre.orbit.domain.repository.PlanetStatusRepository;
import com.mercadolibre.orbit.domain.service.*;
import com.mercadolibre.orbit.domain.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
public class OrbitCalculationServiceImpl implements OrbitCalculationService {

    @Value("${solarsystem.planets_by_solar_system}")
    private int planetsBySolarSystem;


    // Services
    @Autowired
    private PlanetService planetService;

    @Autowired
    private PlanetStatusService planetStatusService;

    @Autowired
    private SolarSystemService solarSystemService;

    @Autowired
    private GeometryService geometryService;





    /**
     * Spin all Solar System planets
     * Calculate orbit & weather conditions; and persist new Status
     *
     * The time are in relative days (days * degrees_per_day) since actual persisted position
     * Is NOT computed with the absolute time since the creation of the Solar System
     *
     * INVASIVE procedure
     *
     * @param solarSystem
     * @param relativeDays
     */
    @Override
    public void spinSolarSystem(SolarSystem solarSystem, int relativeDays) throws InsufficientPlanetsException, SolarSystemNotFound, AmountOfPlanetsStatusException {
        /*
        Planets rotation
         */

        // Check planets necessary for status computation
       if(solarSystemService.countPlanets(solarSystem) != planetsBySolarSystem)
            throw new InsufficientPlanetsException(solarSystem, planetsBySolarSystem);

        // Get rotated Planets positions for Weather computation
        List<Planet> planets = planetService.getFromSolarSystem(solarSystem);
        List<Point> planetsPositions = new ArrayList<>();
        List<PlanetStatus> planetStatuses = new ArrayList<>();

        for(Planet planet : planets) {
            try {
                double degreesByDays = getPlanetRotationDegrees(planet, relativeDays);
                Point point = getPlanetRotationPosition(planet, degreesByDays); // Temporary hard coded degrees

                // Push points to calculate Weather
                planetsPositions.add(point);

                // Push Planets Status
                PlanetStatus planetStatus = new PlanetStatus();
                planetStatus.setPlanet(planet);
                planetStatus.setPositionX(point.getX());
                planetStatus.setPositionY(point.getY());

                planetStatuses.add(planetStatus);
            } catch (PlanetWithoutSolarSystemException e) {
                throw new OrbitCalculationServiceRuntimeException(e.getMessage());
            }
        }

        // Perimeter can be computed with only 3 positions
        if(planetStatuses.size() > 3)
            throw new AmountOfPlanetsStatusException("Calculate Perimeter", 3, planetStatuses.size());

        // Compute perimeter
        double perimeter = getPlanetsPerimeter(
                planetStatuses.get(0),
                planetStatuses.get(1),
                planetStatuses.get(2)
        );

        /*
        Weather conditions
         */
        Weather weather = null;
        try {
            weather = getWeatherCondition(planetsPositions);
        } catch (InsufficientPlanetsPositionException e) {
            throw new OrbitCalculationServiceRuntimeException("Insufficient Planets positions for Weather calculation");
        }


        /*
        Set Weather conditions and persist
         */
        for(PlanetStatus ps : planetStatuses) {
            ps.setWeatherStatus(weather.getWeatherStatus());

            // If has Storm calculate intensity using the perimeter value of Planets
            if(weather.getWeatherStatus().equals(WeatherStatus.RAINFALL))
                weather.setIntensity(perimeter);

            // Persist in database
            planetStatusService.create(ps);
        }

    }





    /**
     * Calculate Weather conditions for Solar System
     *
     * @param planetsPositions
     * @return
     * @throws InsufficientPlanetsException
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

        // Rotate Planet using Planet Status last position & Solar System gravity center
        PlanetStatus planetStatus = planetStatusService.getLastPlanetStatus(planet);
        Point startedPlanetPosition = new Point(planetStatus.getPositionX(), planetStatus.getPositionY());

        // Return new rotated Planet Position
        return geometryService.rotate(startedPlanetPosition, gravityCenter,
                planet.getRotationDirection().equals(ClockDirection.CLOCKWISE) ? degrees : degrees * (-1)); // If is COUNTERCLOCKWISE change sign
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
                planetStatusService.getLastPlanetStatus(p1),
                planetStatusService.getLastPlanetStatus(p2),
                planetStatusService.getLastPlanetStatus(p3)
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
     * @throws InsufficientPlanetsException
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
