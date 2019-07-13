package com.mercadolibre.orbit.domain.model.geometry;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "triangles")
public class Triangle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Point p1;

    @ManyToOne
    @NotNull
    private Point p2;

    @ManyToOne
    @NotNull
    private Point p3;


    /**
     * Constructors
     */

    public Triangle() {
    }

    public Triangle(Point p1, Point p2, Point p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }


    /**
     * Getters & Setters
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Point getP1() {
        return p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public Point getP2() {
        return p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public Point getP3() {
        return p3;
    }

    public void setP3(Point p3) {
        this.p3 = p3;
    }

}
