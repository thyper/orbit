package com.mercadolibre.orbit.domain.service;

import com.mercadolibre.orbit.domain.enums.ClockDirection;
import com.mercadolibre.orbit.domain.enums.WeatherStatus;
import com.mercadolibre.orbit.domain.model.jpa.Planet;
import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;
import com.mercadolibre.orbit.domain.model.transients.Sphere;
import com.mercadolibre.orbit.domain.model.transients.Weather;
import com.mercadolibre.orbit.domain.model.transients.Point;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsPositionException;
import com.mercadolibre.orbit.domain.service.exception.PlanetNotFoundException;
import com.mercadolibre.orbit.domain.service.exception.PlanetWithoutSolarSystemException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class OrbitCalculationServiceTest  extends GenericTest {

    @Autowired
    private SolarSystemService solarSystemService;

    @Autowired
    private PlanetService planetService;

    @Autowired
    private PlanetStatusService planetStatusService;


    @Autowired
    private OrbitCalculationService orbitCalculationService;

    @Autowired
    private WeatherService weatherService;



    @Test
    public void testPlanetWeatherConditions() throws InsufficientPlanetsPositionException {
        // Set Sun position
        Sphere gravityCenter = new Sphere(0D, 0D, 100D);
        List<PlanetStatus> rainfallPlanetStatuses = createPlanetStatusesForRainFallWeatherStatus();
        List<PlanetStatus> droughPlanetStatuses = createPlanetStatusesForDroughWeatherStatus();
        List<PlanetStatus> optimalPlanetStatuses = createPlanetStatusesForOptimalWeatherStatus();



        // Test Weather
        Weather rainfallWeather = weatherService.getWeatherCondition(gravityCenter, rainfallPlanetStatuses);
        Weather droughWeather = weatherService.getWeatherCondition(gravityCenter, droughPlanetStatuses);
        Weather optimalhWeather = weatherService.getWeatherCondition(gravityCenter, optimalPlanetStatuses);

        // Tests
        Assert.assertEquals(rainfallWeather.getWeatherStatus(), WeatherStatus.RAINFALL);
        Assert.assertEquals(droughWeather.getWeatherStatus(), WeatherStatus.DROUGHT);
        Assert.assertEquals(optimalhWeather.getWeatherStatus(), WeatherStatus.OPTIMAL);
    }



    @Test
    public void testGetPlanetRotationPosition() throws PlanetWithoutSolarSystemException, PlanetNotFoundException {

        SolarSystem solarSystem = new SolarSystem();
        solarSystem.setName("Solar System Test");
        solarSystem.setCreationDate(new Date(2019, 8, 28));
        solarSystem.setPosX(0D);
        solarSystem.setPosY(0D);

        // Save SolarSystem
        solarSystem = solarSystemService.createSolarSystem(solarSystem);


        Planet planet = new Planet();
        planet.setName("Ferengis Test Planet");
        planet.setSolarSystem(solarSystem);
        planet.setDegreesPerDay(3D);
        planet.setSunDistance(3000D);
        planet.setRotationDirection(ClockDirection.COUNTERCLOCKWISE);

        // Save Planet
        planet = planetService.createPlanet(planet);


        PlanetStatus planetStatus = new PlanetStatus();
        planetStatus.setPlanet(planet);
        planetStatus.setDate(new Date(2019, 7, 18));
        planetStatus.setPositionX(1D);
        planetStatus.setPositionY(0D);
        planetStatus.setWeatherStatus(WeatherStatus.DROUGHT);


        // Save PlanetStatus
        planetStatusService.create(planetStatus);



        // Rotate and Assert results
        Point point = orbitCalculationService.getPlanetRotationPosition(planet,  90);

        /*
        If a Planet is in [1x, 0y] position and we rotate it 90°
        the planet must end in [0x, 1y] position
         */
        Assert.assertEquals(0D, point.getX(), 0.1D);
        Assert.assertEquals(-1D, point.getY(), 0.1D);
    }



    /**
     * Test Planets perimeter
     */
    @Test
    public void testPlanetsPerimeter() {
        PlanetStatus planetStatus1 = new PlanetStatus();
        PlanetStatus planetStatus2 = new PlanetStatus();
        PlanetStatus planetStatus3 = new PlanetStatus();

        double expectedPerimeter = 6.47;

        planetStatus1.setPositionX(1D);
        planetStatus1.setPositionY(-1D);

        planetStatus2.setPositionX(0D);
        planetStatus2.setPositionY(1D);

        planetStatus3.setPositionX(-1D);
        planetStatus3.setPositionY(-1D);


        // Assert if Planet are aligned

        Assert.assertEquals(expectedPerimeter,
                orbitCalculationService.getPlanetsPerimeter(
                        planetStatus1,
                        planetStatus2,
                        planetStatus3
                ),
                0.003D);
    }

    /**
     * Test Planets alignment
     */
    @Test
    public void testPlanetsPositionAlignment() throws InsufficientPlanetsPositionException {
        List<Sphere> planetsPositions = new ArrayList<>();
        planetsPositions.add(new Sphere(1D, 0D, 10D));
        planetsPositions.add(new Sphere(2D, 0D, 10D));
        planetsPositions.add(new Sphere(3D, 0D, 10D));

        // Assert if planets are aligned
        Assert.assertTrue(orbitCalculationService.areAligned(planetsPositions));

        // Change one Position so they are not aligned anymore
        planetsPositions.get(1).setY(1D);

        // Assert if Planets are NOT aligned
        Assert.assertFalse(orbitCalculationService.areAligned(planetsPositions));
    }


    /**
     * Calculate how many degrees a planet spin in X days
     */
    @Test
    public void testPlanetRotationDegreesRuleOfThree() {
        int days = 255;

        Planet planet = new Planet();
        planet.setName("Ferengis Test Planet");
        planet.setDegreesPerDay(3D);
        planet.setSunDistance(3000D);

        double degreesRotated = days * planet.getDegreesPerDay();

        Assert.assertEquals(degreesRotated,
                orbitCalculationService.getPlanetRotationDegrees(planet, days),
                0D);
    }






    private List<PlanetStatus> createPlanetStatusesForRainFallWeatherStatus() {
        // Create PlanetStatus
        PlanetStatus ps1 = new PlanetStatus();
        ps1.setPositionX(1D);
        ps1.setPositionY(-1D);

        PlanetStatus ps2 = new PlanetStatus();
        ps2.setPositionX(-1D);
        ps2.setPositionY(-1D);

        PlanetStatus ps3 = new PlanetStatus();
        ps3.setPositionX(0D);
        ps3.setPositionY(1D);

        // Push Planet Statuses in array
        List<PlanetStatus> planetStatuses = new ArrayList<>();
        planetStatuses.add(ps1);
        planetStatuses.add(ps2);
        planetStatuses.add(ps3);

        return planetStatuses;
    }

    private List<PlanetStatus> createPlanetStatusesForDroughWeatherStatus() {
        // Create PlanetStatus
        PlanetStatus ps1 = new PlanetStatus();
        ps1.setPositionX(1D);
        ps1.setPositionY(0D);

        PlanetStatus ps2 = new PlanetStatus();
        ps2.setPositionX(2D);
        ps2.setPositionY(0D);

        PlanetStatus ps3 = new PlanetStatus();
        ps3.setPositionX(3D);
        ps3.setPositionY(0D);

        // Push Planet Statuses in array
        List<PlanetStatus> planetStatuses = new ArrayList<>();
        planetStatuses.add(ps1);
        planetStatuses.add(ps2);
        planetStatuses.add(ps3);

        return planetStatuses;
    }

    private List<PlanetStatus> createPlanetStatusesForOptimalWeatherStatus() {
        // Create PlanetStatus
        PlanetStatus ps1 = new PlanetStatus();
        ps1.setPositionX(1D);
        ps1.setPositionY(1D);

        PlanetStatus ps2 = new PlanetStatus();
        ps2.setPositionX(2D);
        ps2.setPositionY(1D);

        PlanetStatus ps3 = new PlanetStatus();
        ps3.setPositionX(3D);
        ps3.setPositionY(1D);

        // Push Planet Statuses in array
        List<PlanetStatus> planetStatuses = new ArrayList<>();
        planetStatuses.add(ps1);
        planetStatuses.add(ps2);
        planetStatuses.add(ps3);

        return planetStatuses;
    }


}
