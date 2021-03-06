package com.mercadolibre.orbit.domain.service.impl;

import com.mercadolibre.orbit.domain.enums.TriangleType;
import com.mercadolibre.orbit.domain.model.transients.Sphere;
import com.mercadolibre.orbit.domain.model.transients.Triangle;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsException;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsPositionException;
import com.mercadolibre.orbit.domain.util.GeometryUtils;
import com.mercadolibre.orbit.domain.util.TriangleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class TriangulationOrbitCalculationService extends AbstractOrbitCalculationServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(TriangulationOrbitCalculationService.class);

    /**
     * Check if all planets positions in a given list are aligned
     *
     * @param spherePositions
     * @return
     * @throws InsufficientPlanetsException
     */
    @Override
    public boolean areAligned(List<Sphere> spherePositions) throws InsufficientPlanetsPositionException {

        if(spherePositions.size() < 3)
            throw new InsufficientPlanetsPositionException(3, spherePositions.size());

        boolean aligned = true;

        for(int i = 2; i < spherePositions.size(); i++) {

            Sphere s1 = spherePositions.get(i);
            Sphere s2 = spherePositions.get(i -1);
            Sphere s3 = spherePositions.get(i - 2);

            Triangle t = new Triangle(s1, s2, s3);

            /**
             * ALIGNMENT DETECTION
             * If the Planets form a RECTANGLE or EQUILATERAL Triangle
             * the three of them are aligned ONLY if two of them are collisioning
             */
            if(TriangleUtils.getTriangleType(t) == TriangleType.EQUILATERAL ||
                    TriangleUtils.getTriangleType(t) == TriangleType.RECTANGLE) {

                // If two of them are collisioning are aligned with the third
                if(GeometryUtils.detectCollision(s1, s2) || GeometryUtils.detectCollision(s2, s3) ||
                        GeometryUtils.detectCollision(s3, s1))
                    return true;
                else return false;
            }

            // A Triangle could be in 3 positions
            // Take always the smaller height of the Triangle because
            // if Spheres are aligned or close to be aligned then the smaller height
            // of the Triangle must be taken
            double height = TriangleUtils.getScaleneTriangleSmallerHeight(t);



            /**
             * ALIGNMENT DETECTION
             * For this alignment detection approach it uses one dimension to detect collision between two Spheres
             * The distance between two Spheres is determined by the height of the Triangle the three of the Planets form
             * It segregates by two Spheres and detect collision in the one dimension
             * If the three of them are collisioning in the one dimension where the distance is determined by the height of
             * the Triangle, then there must be an alignment taking place
             */
            aligned &= GeometryUtils.detectCollision(height, s1.getRadius(), s2.getRadius()) &&
                    GeometryUtils.detectCollision(height, s2.getRadius(), s3.getRadius()) &&
                    GeometryUtils.detectCollision(height, s3.getRadius(), s1.getRadius());
        }

        return aligned;
    }

}
