package com.mercadolibre.orbit.domain.service;

import com.mercadolibre.orbit.domain.enums.ClockDirection;
import com.mercadolibre.orbit.domain.model.jpa.Planet;
import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;
import com.mercadolibre.orbit.domain.repository.PlanetRepository;
import com.mercadolibre.orbit.domain.repository.SolarSystemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.Calendar;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrbitApplicationTests {

	@Autowired
    private PlanetRepository planetRepository;

	@Autowired
    private SolarSystemRepository solarSystemRepository;




	@Test
	public void entityCreationAndRelationShip() {
	    SolarSystem solarSystem = new SolarSystem();
	    solarSystem.setName("Solar System Name");
	    solarSystem.setPosX(0D);
	    solarSystem.setPosY(0D);
	    solarSystem.setCreationDate(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));

	    // Save Solar System
        solarSystem = solarSystemRepository.save(solarSystem);

        Assert.notNull(solarSystem.getId());




        Planet planet = new Planet();
        planet.setName("Planet Name");
        planet.setDegreesPerDay(10D);
        planet.setSolarSystem(solarSystem);
        planet.setRotationDirection(ClockDirection.CLOCKWISE);
        planet.setSunDistance(10D);

        // Save Planet
        planetRepository.save(planet);




        SolarSystem ss = solarSystemRepository.findById(solarSystem.getId()).orElse(null);
        System.out.println(String.format("PLANETS: %s", ss.getPlanets().size()));
        System.out.println(String.format("SolarSystem creation Date: %s", solarSystem.getCreationDate().toString()));
	}

}
