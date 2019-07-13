package com.mercadolibre.orbit.domain.service.impl;

import com.mercadolibre.orbit.domain.enums.ClockDirection;
import com.mercadolibre.orbit.domain.model.geometry.Point;
import com.mercadolibre.orbit.domain.model.geometry.Triangle;
import com.mercadolibre.orbit.domain.service.GeometryService;
import org.springframework.stereotype.Service;




@Service
public class GeometryServiceImpl implements GeometryService {


    /**
     * LOCAL Point rotation about a GLOBAL gravity center
     *
     * @param point
     * @param gravityCenter
     * @param clockDirection
     * @param degrees
     * @return
     */
    @Override
    public Point rotate(Point point, Point gravityCenter, ClockDirection clockDirection, Double degrees) {
        double newX = Math.cos(degrees) * (point.getX() - gravityCenter.getX()) -
                Math.sin(degrees) * (point.getY() - gravityCenter.getY()) +
                gravityCenter.getX();

        double newY = Math.sin(degrees) * (point.getX() - gravityCenter.getX()) +
                Math.cos(degrees) * (point.getY() - gravityCenter.getY()) +
                gravityCenter.getY();

        return new Point(newX, newY);
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
        return areCollinear(p1, p2, p3) && areCollinear(p2, p3, p4);
    }


    /**
     * Compute Triangle & Point collision by triangles orientation
     * http://www.dma.fi.upm.es/personal/mabellanas/tfcs/kirkpatrick/Aplicacion/algoritmos.htm
     *
     * @param triangle
     * @param point
     * @return
     */
    @Override
    public boolean detectCollision(Triangle triangle, Point point) {

        // Sliced triangles with Point
        Triangle t1 = new Triangle(triangle.getP1(), triangle.getP2(), point);
        Triangle t2 = new Triangle(triangle.getP2(), triangle.getP3(), point);
        Triangle t3 = new Triangle(triangle.getP3(), triangle.getP1(), point);

        return triangleClockOrientation(triangle) == triangleClockOrientation(t1) &&
                triangleClockOrientation(t1) == triangleClockOrientation(t2) &&
                triangleClockOrientation(t2) == triangleClockOrientation(t3);
    }



    private double triangleOrientation(Triangle triangle) {
        return (triangle.getP1().getX() - triangle.getP3().getX()) *
                (triangle.getP2().getY() - triangle.getP3().getY()) -
                (triangle.getP1().getY() - triangle.getP3().getY()) *
                (triangle.getP2().getX() - triangle.getP3().getX());
    }

    private ClockDirection triangleClockOrientation(Triangle triangle) {
        return triangleOrientation(triangle) > 0 ? ClockDirection.CLOCKWISE : ClockDirection.COUNTERCLOCKWISE;
    }

}
