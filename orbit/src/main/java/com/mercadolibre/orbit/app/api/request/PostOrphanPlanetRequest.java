package com.mercadolibre.orbit.app.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercadolibre.orbit.domain.enums.ClockDirection;

public class PostOrphanPlanetRequest {

    @JsonProperty("name")
    protected String name;

    @JsonProperty("sun_distance")
    protected Double sunDistance;

    @JsonProperty("radius")
    protected Double radius;

    @JsonProperty("degrees_per_day")
    protected Double degreesPerDay;

    @JsonProperty("rotation_direction")
    protected ClockDirection rotationDirection;



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

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
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
