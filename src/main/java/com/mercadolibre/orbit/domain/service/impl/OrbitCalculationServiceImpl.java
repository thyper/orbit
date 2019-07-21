package com.mercadolibre.orbit.domain.service.impl;

import com.mercadolibre.orbit.app.util.DateUtil;
import com.mercadolibre.orbit.domain.enums.ClockDirection;
import com.mercadolibre.orbit.domain.enums.SolarSystemStatus;
import com.mercadolibre.orbit.domain.enums.SpiningStatus;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;


@Service
public class OrbitCalculationServiceImpl implements OrbitCalculationService {

    private static final Logger logger = LoggerFactory.getLogger(OrbitCalculationServiceImpl.class);

    private final int planetsBySolarSystem = 3;


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
     * Procedure that Spin every Solar System that wasn't updated till 'toDate'
     *
     * @param toDate
     */
    @Override
    public SpiningStatus spinSolarSystems(Date toDate) {
        logger.info("Spining solar Systems");

        List<SolarSystem> solarSystems = solarSystemService.getAll();

        int solarSystemsRotatedSuccessfully = 0;

        for(SolarSystem ss : solarSystems) {
            logger.info("Spining SolarSystem {}-{}", ss.getId(), ss.getName());

            // Get last Date when SolarSystem was updated
            int daysWithoutBeenRotated = 0;
            Date ssRotatedToDate = null;

            if(ss.getRotatedToDate() != null) {
                ssRotatedToDate = ss.getRotatedToDate();
                daysWithoutBeenRotated = DateUtil.getDaysDifference(DateUtil.dateToLocalDate(ssRotatedToDate),
                        DateUtil.dateToLocalDate(toDate));
                logger.info("{} days without being rotated", daysWithoutBeenRotated);
            }else {
                ssRotatedToDate = ss.getCreationDate();
                logger.info("Solar system ever was rotated before");
            }

            // If SolarSystem is updated continue
            if(daysWithoutBeenRotated <= 0)
                continue;

            // Rotate all the days between
            int daysRotatedSuccessFully = 0;
            for(int i = 0; i < daysWithoutBeenRotated; i++) {
                logger.info("Spinning SolarSystem {}-{} | {}/{} day - {}",
                        ss.getId(), ss.getName(),
                        i, daysWithoutBeenRotated,
                        DateUtil.sumDays(ss.getRotatedToDate(), i));

                try {
                    spinSolarSystemOneDay(ss);
                    daysRotatedSuccessFully++;

                    // Set till the Solar System was updated
                    // So we know when was its last Planet Status positions update
                    ss.setRotatedToDate(DateUtil.sumDays(ssRotatedToDate, daysRotatedSuccessFully));

                    if(!ss.getStatus().equals(SolarSystemStatus.NEEDS_REVISION))
                        ss.setStatus(SolarSystemStatus.OK);

                    logger.info("Saving solar system {}-{} rotation till {}",
                            ss.getId(), ss.getName(),
                            ss.getRotatedToDate());

                    try {
                        solarSystemService.save(ss);
                    } catch (ResourceNotFoundException e) {
                        ss.setStatus(SolarSystemStatus.NEEDS_REVISION);
                        String msg = String.format("Couldn't persist SolarSystem with ID-%s.. REASONS: %s",
                                ss.getId(), e.getMessage());
                        logger.error(msg);
                    }
                } catch (InsufficientPlanetsException | AmountOfPlanetsStatusException e) {
                    ss.setStatus(SolarSystemStatus.NEEDS_REVISION);

                    logger.error(String.format("SolarSystem with ID-%s NEED REVISION.. REASONS: %s",
                            ss.getId(), e.getMessage()));

                    try {
                        solarSystemService.save(ss);
                    } catch (ResourceNotFoundException e1) {
                        String msg = String.format("Couldn't persist SolarSystem with ID-%s.. REASONS: %s",
                                ss.getId(), e1.getMessage());
                        logger.error(msg);
                    }
                } catch (SolarSystemNotFound solarSystemNotFound) {
                    logger.error(String.format("SolarSystem with ID-%s not registered. NEEDS REVISION. REASONS: %s",
                            ss.getId(), solarSystemNotFound.getMessage()));
                }
            }

            if(daysRotatedSuccessFully == daysWithoutBeenRotated)
                solarSystemsRotatedSuccessfully++;
        }

        if(solarSystemsRotatedSuccessfully < solarSystems.size())
            return SpiningStatus.REVISION;
        else
            return SpiningStatus.OK;
    }



    /**
     * Spin all Solar System planets
     * Calculate orbit & weather conditions; and persist new Status
     *
     * Takes actual Planet positions and Rotate ONLY ONE day since last persisted position
     * Is NOT computed with the absolute time since the creation of the Solar System !!
     *
     *
     * @param solarSystem
     */
    @Transactional
    private void spinSolarSystemOneDay(SolarSystem solarSystem) throws InsufficientPlanetsException, SolarSystemNotFound, AmountOfPlanetsStatusException {
        /*
        Planets rotation
         */
        final int solarSystemRelativeDays = 1; // Spin only One Day

        // Check planets necessary for status computation
        try {
            if(solarSystemService.countPlanets(solarSystem) != planetsBySolarSystem)
                 throw new InsufficientPlanetsException(solarSystem, planetsBySolarSystem);
        } catch (ResourceNotFoundException e) {
            throw new SolarSystemNotFound(e.getMessage());
        }

        // Get rotated Planets positions for Weather computation
        List<Planet> planets = null;

        try {
            planets = planetService.getFromSolarSystem(solarSystem);
        } catch (ResourceNotFoundException e) {
            throw new SolarSystemNotFound(e.getMessage());
        }

        List<PlanetStatus> planetStatuses = new ArrayList<>();

        for(Planet planet : planets) {
            try {
                logger.info("Calculating new rotation point for planet {}-{}. From Solar System {}-{}",
                        planet.getId(),
                        planet.getName(),
                        planet.getSolarSystem().getId(),
                        planet.getSolarSystem().getName()
                );

                double degreesByDays = getPlanetRotationDegrees(planet, solarSystemRelativeDays);
                Point point = null;     // Calculate new Planet position

                point = getPlanetRotationPosition(planet, degreesByDays);

                // Push Planets Status
                PlanetStatus planetStatus = new PlanetStatus();
                planetStatus.setPlanet(planet);
                planetStatus.setPositionX(point.getX());
                planetStatus.setPositionY(point.getY());
                planetStatus.setDate(DateUtil.sumDays(solarSystem.getRotatedToDate(), 1));

                planetStatuses.add(planetStatus);
            } catch (PlanetWithoutSolarSystemException | PlanetNotFoundException e) {
                logger.error(String.format("Couldn persist new PlanetStatus for Planet ID-%s .. REASONS: %s",
                        planet.getId(), e.getMessage()));
                throw new OrbitCalculationServiceRuntimeException(e.getMessage());
            }
        }

        // Perimeter can be computed with only 3 positions
        if(planetStatuses.size() > 3)
            throw new AmountOfPlanetsStatusException("Calculate Perimeter", 3, planetStatuses.size());


        /*
        Weather conditions
         */
        logger.info("Proceeding to compute weather for SolarSystem({}-{})", solarSystem.getId(), solarSystem.getName());

        Weather weather = null;
        try {
            weather = getWeatherCondition(
                    new Point(solarSystem.getPosX(), solarSystem.getPosY()),
                    planetStatuses
            );
        } catch (InsufficientPlanetsPositionException e) {
            throw new OrbitCalculationServiceRuntimeException("Insufficient Planets positions for Weather calculation");
        }

        logger.info("Weather computed: {}", weather.getWeatherStatus().toString());


        /*
        Set Weather conditions and persist
         */
        logger.info("Setting Weather {} for Planets Statuses", weather.getWeatherStatus().toString());

        for(PlanetStatus ps : planetStatuses) {
            if(weather == null)
                ps.setWeatherStatus(null);
            else
                ps.setWeatherStatus(weather.getWeatherStatus());

            planetStatusService.create(ps);
        }

        logger.info("SolarSystem({}-{}) planet statuses persisted",
                solarSystem.getId(), solarSystem.getName());
    }





    /**
     * Calculate Weather conditions for Solar System
     *
     * @param gravityCenter
     * @param planetStatuses
     * @return Weather
     * @throws InsufficientPlanetsException
     */
    @Override
    public Weather getWeatherCondition(Point gravityCenter, List<PlanetStatus> planetStatuses) throws InsufficientPlanetsPositionException {

        // Check planets necessary for status computation
        if(planetStatuses.size() != planetsBySolarSystem)
            throw new InsufficientPlanetsPositionException(planetsBySolarSystem, planetStatuses.size());

        // Calculate Weather
        Weather weather = new Weather();


        // Create Points positions of Planets
        List<Point> planetsPositions = new ArrayList<>();
        planetsPositions.add(new Point(planetStatuses.get(0).getPositionX(), planetStatuses.get(0).getPositionY()));
        planetsPositions.add(new Point(planetStatuses.get(1).getPositionX(), planetStatuses.get(1).getPositionY()));
        planetsPositions.add(new Point(planetStatuses.get(2).getPositionX(), planetStatuses.get(2).getPositionY()));

        boolean planetsAligned = areAligned(planetsPositions);
        if(areAligned(planetsPositions)) {
            // Add sun
            planetsPositions.add(gravityCenter);

            // Check alignment with gravity center
            if(areAligned(planetsPositions)) {
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
            if(geometryService.detectCollision(planetsTriangle, gravityCenter)) {
                // If sun is inside Triangle
                double perimeter = getPlanetsPerimeter(planetStatuses.get(0),
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
    public Point getPlanetRotationPosition(Planet planet, double degrees) throws PlanetWithoutSolarSystemException, PlanetNotFoundException {

        // Get the Solar System gravity center
        SolarSystem solarSystem = planet.getSolarSystem();

        if(solarSystem == null)
            throw new PlanetWithoutSolarSystemException(planet);

        Point gravityCenter = new Point(solarSystem.getPosX(), solarSystem.getPosY());

        // Rotate Planet using Planet Status last position & Solar System gravity center
        PlanetStatus planetStatus = null;
        try {
            planetStatus = planetStatusService.getLastPlanetStatus(planet);
        } catch (ResourceNotFoundException e) {
            throw new PlanetNotFoundException(e.getMessage());
        }

        Point startedPlanetPosition = new Point(planetStatus.getPositionX(), planetStatus.getPositionY());

        logger.info("Last Planet ({}-{}) Status position: x{} y{}",
                planet.getId(), planet.getName(),
                startedPlanetPosition.getX(), startedPlanetPosition.getY());

        // Return new rotated Planet Position
        double degreesToRotate = planet.getRotationDirection().equals(ClockDirection.CLOCKWISE) ? degrees : degrees * (-1); // If is COUNTERCLOCKWISE change sign

        logger.info("Rotating {} degrees", degreesToRotate);
        Point planetNewPoint = geometryService.rotate(startedPlanetPosition, gravityCenter, degreesToRotate);

        logger.info("Planet {}-{} new point: x{} y{}",
                planet.getId(), planet.getName(),
                planetNewPoint.getX(), planetNewPoint.getY());

        return planetNewPoint;
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
    public double getPlanetsPerimeter(Planet p1, Planet p2, Planet p3) throws ResourceNotFoundException {
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
