package com.mercadolibre.orbit.domain.service;

import com.mercadolibre.orbit.domain.enums.ClockDirection;
import com.mercadolibre.orbit.domain.model.Planet;
import com.mercadolibre.orbit.domain.model.PlanetStatus;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.model.geometry.Point;
import com.mercadolibre.orbit.domain.repository.SolarSystemRepository;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsPositionException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class OrbitCalculationServiceTest  extends GenericTest {

    @Autowired
    private PlanetService planetService;

    @Autowired
    private OrbitCalculationService orbitCalculationService;

    @Autowired
    private SolarSystemService solarSystemService;





    @Test
    public void testGetPlanetRotationPosition() {

        SolarSystem solarSystem = new SolarSystem();
        solarSystem.setName("Solar System Test");
        solarSystem.setCreationDate(new Date(2019, 8, 28));
        solarSystem.setPosX(0D);
        solarSystem.setPosY(0D);

        solarSystem = solarSystemService.createSolarSystem(solarSystem);

        SolarSystem ss = solarSystemService.findById(solarSystem.getId());

        Assert.assertNotNull(ss);

        Planet planet = new Planet();
        planet.setName("Ferengis Test Planet");
        planet.setSolarSystem(ss);
        planet.setDegreesPerDay(3D);
        planet.setSunDistance(3000D);
        planet.setRotationDirection(ClockDirection.CLOCKWISE);

        planetService.createPlanet(planet);

        Assert.assertNotNull(planet.getId());
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
        List<Point> planetsPositions = new ArrayList<>();
        planetsPositions.add(new Point(1D, 0D));
        planetsPositions.add(new Point(2D, 0D));
        planetsPositions.add(new Point(3D, 0D));

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




}
