package com.mercadolibre.orbit.domain.service;

import com.mercadolibre.orbit.domain.model.transients.Point;
import com.mercadolibre.orbit.domain.model.transients.Triangle;
import com.mercadolibre.orbit.domain.service.util.GeometryUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GeometryUtilsTest extends GenericTest {



    private final Logger logger = LoggerFactory.getLogger(GeometryUtilsTest.class);



    /**
     * Test if a Point is rotated correctly
     */
    @Test
    public void testPointRotation() {
        double degrees = 180;

        Point gravityCenter = new Point(0.0D, 0.0D);
        Point point = new Point(1.0D, 0.0D);
        Point newPoint = GeometryUtils.rotate(point, gravityCenter, degrees);

        Assert.assertEquals(-1.0D, newPoint.getX(), 0.1D);
        Assert.assertEquals(0.0D, newPoint.getY(), 0.1D);
    }

    /**
     * Test vector magnitude
     */
    @Test
    public void testVectorMagnitude() {
        Point p0 = new Point(0D, 0D);
        Point p1 = new Point(1D, 1D);

        double magnitude = GeometryUtils.getVectorMagnitude(p0, p1);

        Assert.assertEquals(1.4D, magnitude, 0.4D);
    }


    /**
     * Test triangle perimeter calculation
     */
    @Test
    public void testTrianglePerimeter() {
        Triangle triangle = new Triangle(
                new Point(1D, -1D),
                new Point(0D, 1D),
                new Point(-1D, -1D)
        );

        double v1Magnitude = GeometryUtils.getVectorMagnitude(triangle.getP1(), triangle.getP2());
        double v2Magnitude = GeometryUtils.getVectorMagnitude(triangle.getP2(), triangle.getP3());
        double v3Magnitude = GeometryUtils.getVectorMagnitude(triangle.getP3(), triangle.getP1());
        double tPerimeter1 = v1Magnitude + v2Magnitude + v3Magnitude;

        double tPerimeter2 = GeometryUtils.getTrianglePerimeter(triangle);

        Assert.assertEquals(tPerimeter1, tPerimeter2, 0d);
    }

    /**
     * Test points Collinearity
     */
    @Test
    public void testCollinearity() {
        Point p1 = new Point(0d, 0d);
        Point p2 = new Point(1d, 1d);
        Point p3 = new Point(3d, 3d);

        // If points are collinear
        Assert.assertTrue(GeometryUtils.areCollinear(p1, p2, p3));

        // If points are NOT collinear
        p3.setY(2d);
        Assert.assertFalse(GeometryUtils.areCollinear(p1, p2, p3));
    }


    /**
     * Test Point-Triangle Collision
     */
    @Test
    public void testPointTriangleCollision() {
        Triangle triangle = new Triangle(
                new Point(1D, -1D),
                new Point(0D, 1D),
                new Point(-1D, -1D)
        );

        Point point = new Point(0d, 0d);

        // If Point is INSIDE Triangle
        Assert.assertTrue(GeometryUtils.detectCollision(triangle, point));

        // If Point is OUTSIDE Triangle
        point.setX(10d);
        Assert.assertFalse(GeometryUtils.detectCollision(triangle, point));
    }



    @Test
    public void testIfTriangleTypes() {
        // Equilateral Triangle
        Triangle equilateral = new Triangle(new Point(1D, -1D),
                new Point(-1D, -1D),
                new Point(0D, 1D));
    }

}
