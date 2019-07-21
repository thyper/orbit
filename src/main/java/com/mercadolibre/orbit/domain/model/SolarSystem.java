package com.mercadolibre.orbit.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mercadolibre.orbit.domain.enums.SolarSystemStatus;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

@Entity(name = "solar_systems")
@Table(name = "solar_systems")
public class SolarSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @CreationTimestamp
    private Date creationDate;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "solarSystem", orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Planet> planets;

    @NotNull
    private Double posX;

    @NotNull
    private Double posY;

    private Date lastDateRotated;

    private Date rotatedToDate;

    @Enumerated(EnumType.STRING)
    private SolarSystemStatus status;





    /**
     * Getters & Setters
     * @return
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    public Double getPosX() {
        return posX;
    }

    public void setPosX(Double posX) {
        this.posX = posX;
    }

    public Double getPosY() {
        return posY;
    }

    public void setPosY(Double posY) {
        this.posY = posY;
    }

    public Date getLastDateRotated() {
        return lastDateRotated;
    }

    public void setLastDateRotated(Date lastDateRotated) {
        this.lastDateRotated = lastDateRotated;
    }

    public Date getRotatedToDate() {
        return rotatedToDate;
    }

    public void setRotatedToDate(Date rotatedToDate) {
        this.rotatedToDate = rotatedToDate;
    }

    public SolarSystemStatus getStatus() {
        return status;
    }

    public void setStatus(SolarSystemStatus status) {
        this.status = status;
    }
}
