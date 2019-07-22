package com.mercadolibre.orbit.domain.model.transients;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

    public Point(Long x, Long y) {
        this.x = Double.valueOf(x);
        this.y = Double.valueOf(y);
    }


    /**
     * Methods
     */

    public Point add(Point point) {
        return new Point(this.x + point.getX(), this.y + point.getY());
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
