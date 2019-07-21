package com.mercadolibre.orbit.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mercadolibre.orbit.domain.enums.ClockDirection;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity(name = "planets")
@Table(name = "planets")
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Date creationDate;


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

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "planet", orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PlanetStatus> planetStatuses;



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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<PlanetStatus> getPlanetStatuses() {
        return planetStatuses;
    }

    public void setPlanetStatuses(List<PlanetStatus> planetStatuses) {
        this.planetStatuses = planetStatuses;
    }

    public void setSolarSystem(SolarSystem solarSystem) {
        this.solarSystem = solarSystem;
    }


}
