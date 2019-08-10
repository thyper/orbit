package com.mercadolibre.orbit.app.seeder;

import com.mercadolibre.orbit.domain.enums.ClockDirection;
import com.mercadolibre.orbit.domain.model.jpa.Planet;
import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;
import com.mercadolibre.orbit.domain.service.PlanetService;
import com.mercadolibre.orbit.domain.service.SolarSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DbSeeder {

    @Autowired
    private SolarSystemService solarSystemService;

    @Autowired
    private PlanetService planetService;


    private final Logger logger = LoggerFactory.getLogger(DbSeeder.class);



    @EventListener
    public void seed(ContextRefreshedEvent event) {
        logger.info("Seeding Database...");

        SolarSystem solarSystem = seedSolarSystem();
        seedPlanets(solarSystem);

        logger.info("Database seeded");
    }

    private SolarSystem seedSolarSystem() {
        // Don't seed Solar System if one is created
        if(solarSystemService.countSolarSystems() > 0)
            return null;

        logger.info("Seeding Solar System");

        SolarSystem solarSystem = new SolarSystem();
        solarSystem.setName("Milkyway");
        solarSystem.setPosX(0D);
        solarSystem.setPosY(0D);
        solarSystem.setSunRadius(10D);

        // Persist Solar System
        solarSystem = solarSystemService.createSolarSystem(solarSystem);

        logger.info("Solar System {} seeded", solarSystem.getId());

        return solarSystem;
    }

    private void seedPlanets(SolarSystem solarSystem) {
        // Don't seed Planets if there are Planets in database
        if(planetService.countPlanets() > 0)
            return;

        logger.info("Seeding Planets");

        Planet p1 = new Planet();
        Planet p2 = new Planet();
        Planet p3 = new Planet();

        p1.setName("Ferengis");
        p1.setRadius(10D);
        p1.setSunDistance(40D);
        p1.setDegreesPerDay(1D);
        p1.setRotationDirection(ClockDirection.CLOCKWISE);
        p1.setSolarSystem(solarSystem);

        p2.setName("Vulcano");
        p2.setRadius(10D);
        p2.setSunDistance(80D);
        p2.setDegreesPerDay(5D);
        p2.setRotationDirection(ClockDirection.COUNTERCLOCKWISE);
        p2.setSolarSystem(solarSystem);

        p3.setName("Betasoide");
        p3.setRadius(10D);
        p3.setSunDistance(120D);
        p3.setDegreesPerDay(3D);
        p3.setRotationDirection(ClockDirection.CLOCKWISE);
        p3.setSolarSystem(solarSystem);

        planetService.createPlanet(p1);
        planetService.createPlanet(p2);
        planetService.createPlanet(p3);

        logger.info("Planets seeded");
    }

}
