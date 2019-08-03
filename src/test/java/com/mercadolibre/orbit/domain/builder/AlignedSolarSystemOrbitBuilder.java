package com.mercadolibre.orbit.domain.builder;

import com.mercadolibre.orbit.domain.enums.ClockDirection;
import com.mercadolibre.orbit.domain.enums.WeatherStatus;
import com.mercadolibre.orbit.domain.model.jpa.Planet;
import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlignedSolarSystemOrbitBuilder implements SolarSystemOrbitBuilder {

    private SolarSystem solarSystem;
    private List<Planet> planets;
    private List<PlanetStatus> planetStatuses;

    private double sunDelta;

    public AlignedSolarSystemOrbitBuilder(double sunDelta) {
        this.sunDelta = sunDelta;
    }



    @Override
    public void reset() {
        this.solarSystem = new SolarSystem();
        this.planets = new ArrayList<>();
        this.planetStatuses = new ArrayList<>();
    }

    @Override
    public AlignedSolarSystemOrbitBuilder setSolarSystem() {

        SolarSystem solarSystem = new SolarSystem();
        solarSystem.setName("Milkyway");
        solarSystem.setPosX(0D);
        solarSystem.setPosY(0D);
        solarSystem.setSunRadius(50D);

        this.solarSystem = solarSystem;
        return this;
    }

    @Override
    public AlignedSolarSystemOrbitBuilder setPlanets() {

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

        List<Planet> planets = new ArrayList<>();
        planets.add(p1);
        planets.add(p2);
        planets.add(p3);

        this.planets = planets;

        return this;
    }

    @Override
    public AlignedSolarSystemOrbitBuilder setPlanetsStatuses() {

        Planet p1 = planets.get(0);
        Planet p2 = planets.get(1);
        Planet p3 = planets.get(2);

        PlanetStatus ps1 = new PlanetStatus();
        ps1.setPlanet(p1);
        ps1.setPositionX(p1.getSunDistance());
        ps1.setPositionY(sunDelta);
        ps1.setWeatherStatus(WeatherStatus.DROUGHT);
        ps1.setDate(new Date());

        PlanetStatus ps2 = new PlanetStatus();
        ps2.setPlanet(p2);
        ps2.setPositionX(p2.getSunDistance());
        ps2.setPositionY(sunDelta);
        ps2.setWeatherStatus(WeatherStatus.DROUGHT);
        ps2.setDate(new Date());

        PlanetStatus ps3 = new PlanetStatus();
        ps3.setPlanet(p3);
        ps3.setPositionX(p3.getSunDistance());
        ps3.setPositionY(sunDelta);
        ps3.setWeatherStatus(WeatherStatus.DROUGHT);
        ps3.setDate(new Date());

        planetStatuses = new ArrayList<>();
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
