package com.mercadolibre.orbit.domain.model.transients;

public class Sphere extends Point {

    private double radius;

    public Sphere() {
    }

    public Sphere(double posX, double posY, double radius) {
        this.setX(posX);
        this.setY(posY);
        this.setRadius(radius);
    }



    /*
    Getters & Setters
     */

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setPosX(Double posX) {
        this.setPosX(posX);
    }

    public Double getPosX() {
        return this.getPosX();
    }

    public void setPosY(Double posY) {
        this.setPosY(posY);
    }

    public Double getPosY() {
        return this.getPosY();
    }
}
