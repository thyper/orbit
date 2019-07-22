package com.mercadolibre.orbit.domain.model.jpa;

import com.mercadolibre.orbit.domain.enums.WeatherStatus;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;



@Entity(name = "planets_status")
@Table(name = "planets_status")
public class PlanetStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Date creationDate;

    @NotNull
    private Date date;

    @ManyToOne
    private Planet planet;

    @NotNull
    private Double positionX;

    @NotNull
    private Double positionY;

    @Enumerated(EnumType.STRING)
    private WeatherStatus weatherStatus;

    private Double weatherIntensity;






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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
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

    public WeatherStatus getWeatherStatus() {
        return weatherStatus;
    }

    public void setWeatherStatus(WeatherStatus weatherStatus) {
        this.weatherStatus = weatherStatus;
    }

    public Double getWeatherIntensity() {
        return weatherIntensity;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setWeatherIntensity(Double weatherIntensity) {
        this.weatherIntensity = weatherIntensity;
    }

}
