package com.mercadolibre.orbit.domain.model.geometry;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "points")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double x;

    @NotNull
    private Double y;


    /**
     * Constructors
     */

    public Point() {
    }

    public Point(Double x, Double y) {
        this.x = x;
        this.y = y;
    }



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

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
