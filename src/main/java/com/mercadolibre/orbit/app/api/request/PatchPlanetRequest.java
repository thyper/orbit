package com.mercadolibre.orbit.app.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercadolibre.orbit.domain.enums.ClockDirection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


public class PatchPlanetRequest {

    @JsonProperty(value = "name", required = false)
    private String name;

    @JsonProperty(value = "sun_distance", required = false)
    private Double sunDistance;

    @JsonProperty(value = "degrees_per_day", required = false)
    private Double degreesPerDay;

    @JsonProperty(value = "clock_direction", required = false)
    @Enumerated(EnumType.STRING)
    private ClockDirection rotationDirection;



    /*
    Getters & Setters
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSunDistance() {
        return sunDistance;
    }

    public void setSunDistance(Double sunDistance) {
        this.sunDistance = sunDistance;
    }

    public Double getDegreesPerDay() {
        return degreesPerDay;
    }

    public void setDegreesPerDay(Double degreesPerDay) {
        this.degreesPerDay = degreesPerDay;
    }

    public ClockDirection getRotationDirection() {
        return rotationDirection;
    }

    public void setRotationDirection(ClockDirection rotationDirection) {
        this.rotationDirection = rotationDirection;
    }
}
