package com.mercadolibre.orbit.domain.model;

import com.mercadolibre.orbit.domain.enums.ClockDirection;
import com.mercadolibre.orbit.domain.model.geometry.Point;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity(name = "planets")
@Table(name = "planets")
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Planet planet = (Planet) o;
        return Objects.equals(id, planet.id) &&
                Objects.equals(name, planet.name) &&
                Objects.equals(solarSystem, planet.solarSystem) &&
                Objects.equals(sunDistance, planet.sunDistance) &&
                Objects.equals(degreesPerDay, planet.degreesPerDay) &&
                rotationDirection == planet.rotationDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, solarSystem);
    }

    @NotNull
    private String name;

    @ManyToOne
    private SolarSystem solarSystem;

    @NotNull
    private Double sunDistance;

    @NotNull
    private Double degreesPerDay;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ClockDirection rotationDirection;



    /**
     * Getters & Setters
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public SolarSystem getSolarSystem() {
        return solarSystem;
    }

    public void setSolarSystem(SolarSystem solarSystem) {
        this.solarSystem = solarSystem;
    }
}
