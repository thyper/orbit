package com.mercadolibre.orbit.app.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class PlanetStatusResponse {

    @JsonProperty("planet_id")
    private Long planetId;

    @JsonProperty("planet_name")
    private String planetName;

    @JsonProperty("weather_status")
    private String weatherStatus;

    @JsonProperty("weather_intensity")
    private Double weatherIntensity;

    @JsonProperty("position_x")
    private Double positionX;

    @JsonProperty("position_y")
    private Double positionY;

    @JsonProperty("date")
    private Date date;

    @JsonProperty("creation_date")
    private Date creationDate;


    /*
    Getters & Setters
     */

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getPlanetId() {
        return planetId;
    }

    public void setPlanetId(Long planetId) {
        this.planetId = planetId;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }

    public Double getPositionX() {
        return positionX;
    }

    public void setPositionX(Double positionX) {
        this.positionX = positionX;
    }

    public Double getPositionY() {
        return positionY;
    }

    public void setPositionY(Double positionY) {
        this.positionY = positionY;
    }

    public String getWeatherStatus() {
        return weatherStatus;
    }

    public void setWeatherStatus(String weatherStatus) {
        this.weatherStatus = weatherStatus;
    }

    public Double getWeatherIntensity() {
        return weatherIntensity;
    }

    public void setWeatherIntensity(Double weatherIntensity) {
        this.weatherIntensity = weatherIntensity;
    }
}
