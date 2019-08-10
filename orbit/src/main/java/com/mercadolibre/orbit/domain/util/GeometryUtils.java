package com.mercadolibre.orbit.domain.util;

import com.mercadolibre.orbit.domain.enums.ClockDirection;
import com.mercadolibre.orbit.domain.model.transients.Point;
import com.mercadolibre.orbit.domain.model.transients.Sphere;
import com.mercadolibre.orbit.domain.model.transients.Triangle;

public class GeometryUtils {

    /**
     * Get the angle (radians) of three points
     *
     * @param center
     * @param p0
     * @param p1
     * @return
     */
    public static double getPointsAngle(Point center, Point p0, Point p1) {

        double p0c = Math.sqrt(Math.pow(center.getX()-p0.getX(),2)+
                Math.pow(center.getY()-p0.getY(),2)); // p0->c (b)
        double p1c = Math.sqrt(Math.pow(center.getX()-p1.getX(),2)+
                Math.pow(center.getY()-p1.getY(),2)); // p1->c (a)
        double p0p1 = Math.sqrt(Math.pow(p1.getX()-p0.getX(),2)+
                Math.pow(p1.getY()-p0.getY(),2)); // p0->p1 (c)

        return Math.acos((p1c*p1c+p0c*p0c-p0p1*p0p1)/(2*p1c*p0c));
    }

    /**
     * Convert degrees to radians
     *
     * @param degrees
     * @return
     */
    public static double degreesToRadians(double degrees) {
        return degrees * 0.0174533;
    }

    /**
     * Convert radians to degrees
     *
     * @param radians
     * @return
     */
    public static double radiansToDegrees(double radians) {
        return radians / 0.0174533;
    }

    /**
     * Get a vector magnitude from two Points
     * @param p1
     * @param p2
     * @return
     */
    public static double getVectorMagnitude(Point p1, Point p2) {
        int exponent = 2;
        return Math.sqrt(
                Math.pow(p1.getX() - p2.getX(), exponent) +
                        Math.pow(p1.getY() - p2.getY(), exponent)
        );
    }

    /**
     * LOCAL Point rotation about a GLOBAL gravity center
     *
     * @param point
     * @param gravityCenter
     * @param degrees
     * @return
     */
    public static Point rotate(Point point, Point gravityCenter, double degrees) {

        double radians = GeometryUtils.degreesToRadians(degrees);

        double newX = Math.cos(radians) * (point.getX() - gravityCenter.getX()) -
                Math.sin(radians) * (point.getY() - gravityCenter.getY()) +
                gravityCenter.getX();

        double newY = Math.sin(radians) * (point.getX() - gravityCenter.getX()) +
                Math.cos(radians) * (point.getY() - gravityCenter.getY()) +
                gravityCenter.getY();

        return new Point(newX, newY);
    }

    public static double getTrianglePerimeter(Triangle triangle) {

        double perimeter = 0;

        perimeter += getVectorMagnitude(triangle.getP1(), triangle.getP2());
        perimeter += getVectorMagnitude(triangle.getP2(), triangle.getP3());
        perimeter += getVectorMagnitude(triangle.getP3(), triangle.getP1());

        return perimeter;
    }

    public static boolean areCollinear(Point p1, Point p2, Point p3) {
        return (p1.getY() - p2.getY()) * (p1.getX() - p3.getX()) ==
                (p1.getY() - p3.getY()) * (p1.getX() - p2.getX());
    }

    /**
     * Compute Triangle & Point collision by triangles orientation
     * http://www.dma.fi.upm.es/personal/mabellanas/tfcs/kirkpatrick/Aplicacion/algoritmos.htm
     *
     * @param triangle
     * @param point
     * @return
     */
    public static boolean detectCollision(Triangle triangle, Point point) {

        // Sliced triangles with Point
        Triangle t1 = new Triangle(triangle.getP1(), triangle.getP2(), point);
        Triangle t2 = new Triangle(triangle.getP2(), triangle.getP3(), point);
        Triangle t3 = new Triangle(triangle.getP3(), triangle.getP1(), point);

        return triangleClockOrientation(triangle) == triangleClockOrientation(t1) &&
                triangleClockOrientation(t1) == triangleClockOrientation(t2) &&
                triangleClockOrientation(t2) == triangleClockOrientation(t3);
    }

    /**
     * Compute Sphere-Sphere collision
     * @param s1
     * @param s2
     * @return
     */
    public static boolean detectCollision(Sphere s1, Sphere s2) {
        double spheresDistance = getVectorMagnitude(new Point(s1.getPosX(), s1.getPosY()), new Point(s2.getPosX(), s2.getPosY()));
        return detectCollision(spheresDistance, s1.getRadius(), s2.getRadius());
    }

    public static boolean detectCollision(double distance, double radius1, double radius2) {
        return distance <= (radius1 + radius2);
    }

    private static double triangleOrientation(Triangle triangle) {
        return (triangle.getP1().getX() - triangle.getP3().getX()) *
                (triangle.getP2().getY() - triangle.getP3().getY()) -
                (triangle.getP1().getY() - triangle.getP3().getY()) *
                        (triangle.getP2().getX() - triangle.getP3().getX());
    }

    private static ClockDirection triangleClockOrientation(Triangle triangle) {
        return triangleOrientation(triangle) > 0 ? ClockDirection.CLOCKWISE : ClockDirection.COUNTERCLOCKWISE;
    }


    /**
     * This function acts as a potentiometer
     * Map delta between a 0 & maxValue
     * When delta is 0 the value is at 100. If delta start to increase the value will start to decrease
     * Useful for weather intensity calculation
     *
     * @param delta
     * @param maxValue
     * @return
     */
    public static double potentiometer(double delta, double maxValue) {
        return Math.pow(Math.cos(degreesToRadians(delta)), 2) * maxValue;
    }

}
