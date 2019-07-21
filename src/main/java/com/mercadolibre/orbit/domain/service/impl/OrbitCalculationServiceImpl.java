package com.mercadolibre.orbit.domain.service.impl;

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
            logger.info("Spining SolarSystem {}", ss.getId());

            // Get last Date when SolarSystem was updated
            int daysWithoutBeenRotated = 0;
            Date ssRotatedToDate = null;

            if(ss.getRotatedToDate() != null) {
                logger.info("Calculating days without being rotated");
                ssRotatedToDate = ss.getRotatedToDate();
                daysWithoutBeenRotated = getDaysDifference(dateToLocalDate(ssRotatedToDate), dateToLocalDate(toDate));
            }else {
                ssRotatedToDate = ss.getCreationDate();
                logger.info("Solar system ever was rotated before");
            }
            logger.info("{} days without being rotated", daysWithoutBeenRotated);

            // If SolarSystem is updated continue
            if(daysWithoutBeenRotated <= 0)
                continue;

            // Rotate all the days between
            int daysRotatedSuccessFully = 0;
            for(int i = 0; i < daysWithoutBeenRotated; i++) {
                logger.info("Spinning SolarSystem {} - {} day", ss.getId(), i);
                try {
                    spinSolarSystemOneDay(ss);
                    daysRotatedSuccessFully++;
                    ss.setRotatedToDate(sumDays(ssRotatedToDate, 1));

                    if(!ss.getStatus().equals(SolarSystemStatus.NEEDS_REVISION))
                        ss.setStatus(SolarSystemStatus.OK);

                    logger.info("Saving solar system {}", ss.getId());
                    solarSystemService.save(ss);
                } catch (InsufficientPlanetsException | AmountOfPlanetsStatusException e) {
                    ss.setStatus(SolarSystemStatus.NEEDS_REVISION);
                    solarSystemService.save(ss);

                    e.printStackTrace();
                } catch (SolarSystemNotFound solarSystemNotFound) {
                    solarSystemNotFound.printStackTrace();
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
        int solarSystemRelativeDays = 1;

        // Check planets necessary for status computation
       if(solarSystemService.countPlanets(solarSystem) != planetsBySolarSystem)
            throw new InsufficientPlanetsException(solarSystem, planetsBySolarSystem);

        // Get rotated Planets positions for Weather computation
        List<Planet> planets = planetService.getFromSolarSystem(solarSystem);
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
                Point point = getPlanetRotationPosition(planet, degreesByDays);

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


        /*
        Weather conditions
         */
        Weather weather = null;
        try {
            weather = getWeatherCondition(
                    new Point(solarSystem.getPosX(), solarSystem.getPosY()),
                    planetStatuses
            );
        } catch (InsufficientPlanetsPositionException e) {
            throw new OrbitCalculationServiceRuntimeException("Insufficient Planets positions for Weather calculation");
        }


        /*
        Set Weather conditions and persist
         */
        logger.info("Setting Weather {} for Planets Status", weather.getWeatherStatus().toString());

        for(PlanetStatus ps : planetStatuses) {
            if(weather == null)
                ps.setWeatherStatus(null);
            else
                ps.setWeatherStatus(weather.getWeatherStatus());

            planetStatusService.create(ps);
        }

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
                weather = null;
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


    /**
     * Private local procedures
     */

    private int getDaysDifference(LocalDate di, LocalDate df) {
        return Math.round(DAYS.between(di, df));
    }

    private LocalDate dateToLocalDate(Date date) {
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return localDateTime.toLocalDate();
    }

    private Date sumDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }
}
