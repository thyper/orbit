package com.mercadolibre.orbit.domain.service.util;

import com.mercadolibre.orbit.domain.enums.TriangleType;
import com.mercadolibre.orbit.domain.model.transients.Triangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class TriangleUtils {

    public enum TriangleRectanglePosition {
        UP, DOWN
    }

    public static double getRectangleTriangleHeight(Triangle t, TriangleRectanglePosition position) {

        // Get all corners angles
        int iangle1 = (int) Math.round(GeometryUtils.radiansToDegrees(GeometryUtils.getPointsAngle(t.getP1(), t.getP2(), t.getP3())));
        int iangle2 = (int) Math.round(GeometryUtils.radiansToDegrees(GeometryUtils.getPointsAngle(t.getP2(), t.getP1(), t.getP3())));
        int iangle3 = (int) Math.round(GeometryUtils.radiansToDegrees(GeometryUtils.getPointsAngle(t.getP3(), t.getP1(), t.getP2())));

        // Magnitudes from 90 degrees Point
        double mg1 = 0;
        double mg2 = 0;

        // Depending of the angle, get the magnitudes of the vector from that center Point
        // that has 90 degrees
        if(iangle1 == 90) {
            mg1 = GeometryUtils.getVectorMagnitude(t.getP1(), t.getP2());
            mg2 = GeometryUtils.getVectorMagnitude(t.getP1(), t.getP3());
        }else if(iangle2 == 90) {
            mg1 = GeometryUtils.getVectorMagnitude(t.getP2(), t.getP3());
            mg2 = GeometryUtils.getVectorMagnitude(t.getP2(), t.getP3());
        }else if(iangle3 == 90) {
            mg1 = GeometryUtils.getVectorMagnitude(t.getP3(), t.getP1());
            mg2 = GeometryUtils.getVectorMagnitude(t.getP3(), t.getP2());
        }

        // Depending on the position return the magnitude thet would be
        // the height of the Triangle
        if(position == TriangleRectanglePosition.UP) {
            // If Triangle is UP return the smaller magnitude
            if(mg1 > mg2) return mg1;
            else return mg2;
        }else {
            // If Triangle is DOWN return the greater magnitude
            if(mg1 > mg2) return mg2;
            else return mg1;
        }
    }

    public static double getScaleneTriangleSmallerHeight(Triangle t) {

        // Get vectors magnitude
        double mg1 = GeometryUtils.getVectorMagnitude(t.getP1(), t.getP2());
        double mg2 = GeometryUtils.getVectorMagnitude(t.getP2(), t.getP3());
        double mg3 = GeometryUtils.getVectorMagnitude(t.getP3(), t.getP1());

        // Get all Triangle heights in all different positions
        List<Double> heights = new ArrayList<>();
        heights.add(mg2 * Math.sin(GeometryUtils.getPointsAngle(t.getP2(), t.getP1(), t.getP3())));
        heights.add(mg3 * Math.sin(GeometryUtils.getPointsAngle(t.getP3(), t.getP1(), t.getP2())));
        heights.add(mg1 * Math.sin(GeometryUtils.getPointsAngle(t.getP1(), t.getP2(), t.getP3())));

        // Seek smaller height of Triangles in different positions
        double tSmallerHeight = 0;
        for(int i = 0; i < heights.size(); i++) {
            if(i == 0) {
                tSmallerHeight = heights.get(i);
                continue;
            }
            if(heights.get(i) < tSmallerHeight) tSmallerHeight = heights.get(i);
        }

        return tSmallerHeight;
    }

    public static TriangleType getTriangleType(Triangle triangle) {
        if(isRectangleTriangle(triangle))
            return TriangleType.RECTANGLE;
        else if(isEquilateralTriangle(triangle))
            return TriangleType.EQUILATERAL;
        else if(isScaleneTriangle(triangle))
            return TriangleType.SCALENE;
        else return null;
    }

    public static boolean isRectangleTriangle(Triangle t) {
        int angle1 = (int) Math.round(GeometryUtils.radiansToDegrees(GeometryUtils.getPointsAngle(t.getP1(), t.getP2(), t.getP3())));
        int angle2 = (int) Math.round(GeometryUtils.radiansToDegrees(GeometryUtils.getPointsAngle(t.getP2(), t.getP1(), t.getP3())));
        int angle3 = (int) Math.round(GeometryUtils.radiansToDegrees(GeometryUtils.getPointsAngle(t.getP3(), t.getP1(), t.getP2())));

        if(angle1 == 90 || angle2 == 90 || angle3 == 90)
            return true;
        else return false;
    }

    public static boolean isEquilateralTriangle(Triangle t) {
        double angle1 = GeometryUtils.getPointsAngle(t.getP1(), t.getP2(), t.getP3());
        double angle2 = GeometryUtils.getPointsAngle(t.getP2(), t.getP1(), t.getP3());
        double angle3 = GeometryUtils.getPointsAngle(t.getP3(), t.getP1(), t.getP2());

        return angle1 == angle2 && angle2 == angle3;
    }

    public static boolean isScaleneTriangle(Triangle t) {
        if(!isRectangleTriangle(t) && !isEquilateralTriangle(t))
            return true;
        else return false;
    }

}
