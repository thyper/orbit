package com.mercadolibre.orbit.domain.service.impl;

import com.mercadolibre.orbit.app.util.DateUtil;
import com.mercadolibre.orbit.domain.enums.ClockDirection;
import com.mercadolibre.orbit.domain.enums.SolarSystemStatus;
import com.mercadolibre.orbit.domain.enums.SpiningStatus;
import com.mercadolibre.orbit.domain.model.jpa.Planet;
import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;
import com.mercadolibre.orbit.domain.model.transients.Sphere;
import com.mercadolibre.orbit.domain.model.transients.Weather;
import com.mercadolibre.orbit.domain.model.transients.Point;
import com.mercadolibre.orbit.domain.model.transients.Triangle;
import com.mercadolibre.orbit.domain.service.*;
import com.mercadolibre.orbit.domain.service.exception.*;
import com.mercadolibre.orbit.domain.util.GeometryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public abstract class AbstractOrbitCalculationServiceImpl implements OrbitCalculationService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractOrbitCalculationServiceImpl.class);

    private final int planetsBySolarSystem = 3;


    // Services
    @Autowired
    private PlanetService planetService;

    @Autowired
    private PlanetStatusService planetStatusService;

    @Autowired
    private SolarSystemService solarSystemService;


    @Autowired
    private WeatherService weatherService;


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
            weather = weatherService.getWeatherCondition(
                    new Sphere(solarSystem.getPosX(), solarSystem.getPosY(), solarSystem.getSunRadius()),
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
        Point planetNewPoint = GeometryUtils.rotate(startedPlanetPosition, gravityCenter, degreesToRotate);

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

        return GeometryUtils.getTrianglePerimeter(new Triangle(
                new Point(p1.getPositionX(), p1.getPositionY()),
                new Point(p2.getPositionX(), p2.getPositionY()),
                new Point(p3.getPositionX(), p3.getPositionY())
        ));
    }




    @Override
    public double getPlanetRotationDegrees(Planet planet, int days) {
        return planet.getDegreesPerDay() * days;
    }


}
