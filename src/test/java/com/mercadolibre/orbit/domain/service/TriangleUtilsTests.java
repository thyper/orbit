package com.mercadolibre.orbit.domain.service;

import com.mercadolibre.orbit.domain.enums.TriangleType;
import com.mercadolibre.orbit.domain.model.transients.Point;
import com.mercadolibre.orbit.domain.model.transients.Triangle;
import com.mercadolibre.orbit.domain.service.util.TriangleUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriangleUtilsTests extends GenericTest {

    private static final Logger logger = LoggerFactory.getLogger(TriangleUtilsTests.class);

    @Test
    public void testRectangleTriangleHeight() {

        // RECTANGLE Triangle
        Triangle t = new Triangle(new Point(1D, 0D),
                new Point(0D, 3D),
                new Point(0D, 0D));


        // Assert when is UP
        Assert.assertEquals(3D,
                TriangleUtils.getRectangleTriangleHeight(t, TriangleUtils.TriangleRectanglePosition.UP),
                0D);

        // Assert when is DOWN
        Assert.assertEquals(1D,
                TriangleUtils.getRectangleTriangleHeight(t, TriangleUtils.TriangleRectanglePosition.DOWN),
                0D);
    }

    @Test
    public void testScaleneTriangleHeight() {

        // SCALENE Triangle
        Triangle t = new Triangle(new Point(2D, -1D),
                new Point(1D, 10D),
                new Point(0D, -1D));

        double h = TriangleUtils.getScaleneTriangleSmallerHeight(t);

        // Test
        Assert.assertEquals(2, Math.round(h));
    }

    @Test
    public void testTriangleType() {

        // RECTANGLE Triangle
        Triangle trec = new Triangle(new Point(1D, 0D),
                new Point(0D, 3D),
                new Point(0D, 0D));


        // SCALENE Triangle
        Triangle tscalene = new Triangle(new Point(3D, 0D),
                new Point(2D, 2D),
                new Point(-5D, 0D));


        Assert.assertTrue(TriangleUtils.getTriangleType(trec) == TriangleType.RECTANGLE);
        Assert.assertTrue(TriangleUtils.getTriangleType(tscalene) == TriangleType.SCALENE);
    }

    @Test
    public void testTriangleTypesValidation() {

        // Triangle RECTANGLE
        Triangle t = new Triangle(new Point(1D, 0D),
                new Point(0D, 1D),
                new Point(0D, 0D));

        Assert.assertTrue(TriangleUtils.isRectangleTriangle(t));
    }

}
