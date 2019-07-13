package com.mercadolibre.orbit.domain.service;

import com.mercadolibre.orbit.domain.enums.ClockDirection;
import com.mercadolibre.orbit.domain.model.geometry.Point;
import com.mercadolibre.orbit.domain.model.geometry.Triangle;


public interface GeometryService {

    Point rotate(Point point, Point gravityCenter, ClockDirection clockDirection, Double degrees);

    double getTrianglePerimeter(Triangle triangle);
    double getVectorMagnitude(Point p1, Point p2);

    boolean areCollinear(Point p1, Point p2, Point p3);
    boolean areCollinear(Point p1, Point p2, Point p3, Point p4);
    boolean detectCollision(Triangle triangle, Point point);

}