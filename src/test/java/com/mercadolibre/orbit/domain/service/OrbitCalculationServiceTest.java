package com.mercadolibre.orbit.domain.service;

import com.mercadolibre.orbit.domain.enums.WeatherStatus;
import com.mercadolibre.orbit.domain.model.jpa.Planet;
import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;
import com.mercadolibre.orbit.domain.model.transients.Sphere;
import com.mercadolibre.orbit.domain.model.transients.Weather;
import com.mercadolibre.orbit.domain.model.transients.Point;
import com.mercadolibre.orbit.domain.builder.AlignedSolarSystemOrbitBuilder;
import com.mercadolibre.orbit.domain.builder.SolarSystemOrbitBuilderDirector;
import com.mercadolibre.orbit.domain.builder.TriangledSolarSystemOrbitBuilder;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsPositionException;
import com.mercadolibre.orbit.domain.service.exception.PlanetNotFoundException;
import com.mercadolibre.orbit.domain.service.exception.PlanetWithoutSolarSystemException;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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


    private static final Logger logger = LoggerFactory.getLogger(OrbitCalculationServiceTest.class);



    @Test
    public void testPlanetWeatherConditions() throws InsufficientPlanetsPositionException {

        // Create Builders & Builder Director
        AlignedSolarSystemOrbitBuilder alignedWithSunOrbitBuilder = new AlignedSolarSystemOrbitBuilder(0D);
        AlignedSolarSystemOrbitBuilder alignedWithoutSunOrbitBuilder = new AlignedSolarSystemOrbitBuilder(100D);
        TriangledSolarSystemOrbitBuilder triangledWithSunOrbitBuilder = new TriangledSolarSystemOrbitBuilder(true);
        TriangledSolarSystemOrbitBuilder triangledWithoutSunOrbitBuilder = new TriangledSolarSystemOrbitBuilder(false);
        SolarSystemOrbitBuilderDirector ssOrbitBuilderDirector;

        // Build all Orbits
        ssOrbitBuilderDirector = new SolarSystemOrbitBuilderDirector(alignedWithSunOrbitBuilder);
        ssOrbitBuilderDirector.build();

        ssOrbitBuilderDirector = new SolarSystemOrbitBuilderDirector(alignedWithoutSunOrbitBuilder);
        ssOrbitBuilderDirector.build();

        ssOrbitBuilderDirector = new SolarSystemOrbitBuilderDirector(triangledWithSunOrbitBuilder);
        ssOrbitBuilderDirector.build();

        ssOrbitBuilderDirector = new SolarSystemOrbitBuilderDirector(triangledWithoutSunOrbitBuilder);
        ssOrbitBuilderDirector.build();


        SolarSystem ss = alignedWithSunOrbitBuilder.getSolarSystem();

        // Set Sun position
        Sphere gravityCenter = new Sphere(0D, 0D, 50D);

        // Test Weather
        Weather droughWeather = weatherService.getWeatherCondition(gravityCenter, alignedWithSunOrbitBuilder.getPlanetStatuses());
        Weather optimalhWeather = weatherService.getWeatherCondition(gravityCenter, alignedWithoutSunOrbitBuilder.getPlanetStatuses());
        Weather rainfallWeather = weatherService.getWeatherCondition(gravityCenter, triangledWithSunOrbitBuilder.getPlanetStatuses());
        Weather unknownWeather = weatherService.getWeatherCondition(gravityCenter, triangledWithoutSunOrbitBuilder.getPlanetStatuses());


        // Tests
        Assert.assertEquals(WeatherStatus.DROUGHT, droughWeather.getWeatherStatus());
        Assert.assertEquals(WeatherStatus.OPTIMAL, optimalhWeather.getWeatherStatus());
        Assert.assertEquals(WeatherStatus.RAINFALL, rainfallWeather.getWeatherStatus());
        Assert.assertEquals(WeatherStatus.UNKNOWN, unknownWeather.getWeatherStatus());
    }



    @Test
    public void testGetPlanetRotationPosition() throws PlanetWithoutSolarSystemException, PlanetNotFoundException {

        AlignedSolarSystemOrbitBuilder alignedOrbitBuilder = new AlignedSolarSystemOrbitBuilder(0D);
        SolarSystemOrbitBuilderDirector ssOrbitDirector = new SolarSystemOrbitBuilderDirector(alignedOrbitBuilder);
        ssOrbitDirector.build();

        SolarSystem solarSystem = alignedOrbitBuilder.getSolarSystem();
        List<Planet> planets = alignedOrbitBuilder.getPlanets();
        List<PlanetStatus> planetStatuses = alignedOrbitBuilder.getPlanetStatuses();

        Planet p = planets.get(0);

        // Save Solar System
        solarSystem = solarSystemService.createSolarSystem(solarSystem);
        // Save Planet
        planetService.createPlanet(p, planetStatuses.get(0));
        // Save PlanetStatuses



        // Rotate and Assert results
        Point point = orbitCalculationService.getPlanetRotationPosition(p,  90);

        /*
        If a Planet is in [1x, 0y] position and we rotate it 90°
        the planet must end in [0x, 1y] position
         */
        Assert.assertEquals(0D, point.getX(), 0.1D);
        Assert.assertEquals(p.getSunDistance(), point.getY(), 0.1D);
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
