package com.mercadolibre.orbit.domain.service;

import com.mercadolibre.orbit.domain.model.jpa.Planet;
import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;
import com.mercadolibre.orbit.domain.builder.AlignedSolarSystemOrbitBuilder;
import com.mercadolibre.orbit.domain.builder.SolarSystemOrbitBuilderDirector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrbitApplicationTests {

	@Autowired
    private PlanetService planetService;

	@Autowired
    private SolarSystemService solarSystemService;




	@Test
	public void entityCreationAndRelationShip() {

        AlignedSolarSystemOrbitBuilder alignedOrbitBuilder = new AlignedSolarSystemOrbitBuilder(0D);
        SolarSystemOrbitBuilderDirector alignedOrbitDirector = new SolarSystemOrbitBuilderDirector(alignedOrbitBuilder);
        alignedOrbitDirector.build();

        SolarSystem solarSystem = alignedOrbitBuilder.getSolarSystem();
        List<Planet> planets = alignedOrbitBuilder.getPlanets();
        List<PlanetStatus> planetStatuses = alignedOrbitBuilder.getPlanetStatuses();

        // Save Solar System
        solarSystem = solarSystemService.createSolarSystem(solarSystem);

        // Save Planet
        Planet p = planetService.createPlanet(planets.get(0), planetStatuses.get(0));

        Assert.notNull(solarSystem.getId());
        Assert.notNull(p.getId());
	}

}
