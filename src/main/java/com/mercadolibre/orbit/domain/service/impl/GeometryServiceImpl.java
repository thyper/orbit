package com.mercadolibre.orbit.domain.service.impl;

import com.mercadolibre.orbit.domain.enums.ClockDirection;
import com.mercadolibre.orbit.domain.model.geometry.Point;
import com.mercadolibre.orbit.domain.model.geometry.Triangle;
import com.mercadolibre.orbit.domain.service.GeometryService;
import org.springframework.stereotype.Service;




@Service
public class GeometryServiceImpl implements GeometryService {

    @Override
    public Point rotate(Point point, Point gravityCenter, ClockDirection clockDirection, Double degrees) {
        return null;
    }

    @Override
    public double getTrianglePerimeter(Triangle triangle) {

        double perimeter = 0;

        perimeter += getVectorMagnitude(triangle.getP1(), triangle.getP2());
        perimeter += getVectorMagnitude(triangle.getP2(), triangle.getP3());
        perimeter += getVectorMagnitude(triangle.getP3(), triangle.getP1());

        return perimeter;
    }

    @Override
    public double getVectorMagnitude(Point p1, Point p2) {

        // Vectors sum
        Point psum = new Point();
        psum.setX(p1.getX() + p2.getX());
        psum.setY(p1.getY() + p2.getY());

        // Vector Magnitude
        int exponent = 2;
        return Math.sqrt(Math.pow(psum.getX(), exponent) + Math.pow(psum.getY(), exponent));
    }

    @Override
    public boolean areCollinear(Point p1, Point p2, Point p3) {
        return (p1.getY() - p2.getY()) * (p1.getX() - p3.getX()) == (p1.getY() - p3.getY()) * (p1.getX() - p2.getX());
    }

    @Override
    public boolean areCollinear(Point p1, Point p2, Point p3, Point p4) {
        return false;
    }

    @Override
    public boolean collide(Point point, Triangle triangle) {
        return false;
    }

}
