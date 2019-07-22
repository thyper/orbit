package com.mercadolibre.orbit.domain.model.transients;

import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;

import java.util.List;



public class SolarSystemStatus {

    private SolarSystem solarSystem;
    private Weather weather;
    private List<PlanetStatus> planetStatus;



    /**
     * Getters & Setters
     */

    public SolarSystem getSolarSystem() {
        return solarSystem;
    }

    public void setSolarSystem(SolarSystem solarSystem) {
        this.solarSystem = solarSystem;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public List<PlanetStatus> getPlanetStatus() {
        return planetStatus;
    }

    public void setPlanetStatus(List<PlanetStatus> planetStatus) {
        this.planetStatus = planetStatus;
    }
}
