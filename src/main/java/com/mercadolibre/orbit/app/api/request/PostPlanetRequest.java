package com.mercadolibre.orbit.app.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercadolibre.orbit.domain.enums.ClockDirection;




public class PostPlanetRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("solar_system_id")
    private Long solarSystemId;

    @JsonProperty("sun_distance")
    private Double sunDistance;

    @JsonProperty("degrees_per_day")
    private Double degreesPerDay;

    @JsonProperty("rotation_direction")
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

    public Long getSolarSystemId() {
        return solarSystemId;
    }

    public void setSolarSystemId(Long solarSystemId) {
        this.solarSystemId = solarSystemId;
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
