package com.mercadolibre.orbit.domain.model;


import com.mercadolibre.orbit.domain.enums.WeatherStatus;

public class Weather {

    private WeatherStatus weatherStatus;
    private double intensity;



    /**
     * Getters & Setters
     */

    public WeatherStatus getWeatherStatus() {
        return weatherStatus;
    }

    public void setWeatherStatus(WeatherStatus weatherStatus) {
        this.weatherStatus = weatherStatus;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }
}
