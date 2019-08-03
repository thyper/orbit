package com.mercadolibre.orbit.domain.service.builder;

import com.mercadolibre.orbit.domain.enums.ClockDirection;
import com.mercadolibre.orbit.domain.enums.WeatherStatus;
import com.mercadolibre.orbit.domain.model.jpa.Planet;
import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TriangledSolarSystemOrbitBuilder implements SolarSystemOrbitBuilder {
    private SolarSystem solarSystem;
    private List<Planet> planets;
    private List<PlanetStatus> planetStatuses;

    private boolean sunInside;

    public TriangledSolarSystemOrbitBuilder(boolean sunInside) {
        this.sunInside = sunInside;
    }



    @Override
    public void reset() {
        this.solarSystem = new SolarSystem();
        this.planets = new ArrayList<>();
        this.planetStatuses = new ArrayList<>();
    }

    @Override
    public TriangledSolarSystemOrbitBuilder setSolarSystem() {

        solarSystem.setName("Milkyway");
        solarSystem.setPosX(0D);
        solarSystem.setPosY(0D);
        solarSystem.setSunRadius(50D);

        this.solarSystem = solarSystem;
        return this;
    }

    @Override
    public TriangledSolarSystemOrbitBuilder setPlanets() {

        Planet p1 = new Planet();
        p1.setName("Ferengis");
        p1.setSunDistance(500D);
        p1.setDegreesPerDay(1D);
        p1.setRotationDirection(ClockDirection.CLOCKWISE);
        p1.setSolarSystem(solarSystem);
        p1.setRadius(10D);

        Planet p2 = new Planet();
        p2.setName("Vulcanos");
        p2.setSunDistance(100D);
        p2.setDegreesPerDay(5D);
        p2.setRotationDirection(ClockDirection.COUNTERCLOCKWISE);
        p2.setSolarSystem(solarSystem);
        p2.setRadius(10D);

        Planet p3 = new Planet();
        p3.setName("Betasoide");
        p3.setSunDistance(2000D);
        p3.setDegreesPerDay(3D);
        p3.setRotationDirection(ClockDirection.CLOCKWISE);
        p3.setSolarSystem(solarSystem);
        p3.setRadius(10D);


        planets.add(p1);
        planets.add(p2);
        planets.add(p3);

        this.planets = planets;

        return this;
    }

    @Override
    public TriangledSolarSystemOrbitBuilder setPlanetsStatuses() {

        Planet p1 = planets.get(0);
        Planet p2 = planets.get(1);
        Planet p3 = planets.get(2);

        PlanetStatus ps1 = new PlanetStatus();
        PlanetStatus ps2 = new PlanetStatus();
        PlanetStatus ps3 = new PlanetStatus();


        // If Sun is inside kick all orbits using sun radius difference
        double separationDelta = 100;                                                   // Separation between the three points
        double sunDelta = getSolarSystem().getSunRadius() * separationDelta * 2;        // Delta with sun


        WeatherStatus wStatus = sunInside ? WeatherStatus.RAINFALL : WeatherStatus.UNKNOWN;

        ps1.setPlanet(p1);
        ps1.setWeatherStatus(wStatus);
        ps1.setDate(new Date());
        ps1.setPositionX(sunInside ? 100D : 100D + sunDelta);
        ps1.setPositionY(sunInside ? -100D : -100D + sunDelta);

        ps2.setPlanet(p2);
        ps2.setWeatherStatus(wStatus);
        ps2.setDate(new Date());
        ps2.setPositionX(sunInside ? 0D : 0D + sunDelta);
        ps2.setPositionY(sunInside ? 100D : 100D + sunDelta);

        ps3.setPlanet(p3);
        ps3.setWeatherStatus(wStatus);
        ps3.setDate(new Date());
        ps3.setPositionX(sunInside ? -100D : -100D + sunDelta);
        ps3.setPositionY(sunInside ? -100D : -100D + sunDelta);



        planetStatuses.add(ps1);
        planetStatuses.add(ps2);
        planetStatuses.add(ps3);

        return this;
    }




    /*
    Getters & Setters
     */

    public SolarSystem getSolarSystem() {
        return solarSystem;
    }

    public void setSolarSystem(SolarSystem solarSystem) {
        this.solarSystem = solarSystem;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    public List<PlanetStatus> getPlanetStatuses() {
        return planetStatuses;
    }

    public void setPlanetStatuses(List<PlanetStatus> planetStatuses) {
        this.planetStatuses = planetStatuses;
    }
}
