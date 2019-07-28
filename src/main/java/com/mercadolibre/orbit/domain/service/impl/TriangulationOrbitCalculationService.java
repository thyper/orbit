package com.mercadolibre.orbit.domain.service.impl;

import com.mercadolibre.orbit.domain.enums.TriangleType;
import com.mercadolibre.orbit.domain.model.transients.Point;
import com.mercadolibre.orbit.domain.model.transients.Sphere;
import com.mercadolibre.orbit.domain.model.transients.Triangle;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsException;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsPositionException;
import com.mercadolibre.orbit.domain.service.util.GeometryUtils;
import com.mercadolibre.orbit.domain.service.util.TriangleUtils;
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

            // If any of the Planets form a triangle that is not a Scalene then they are not aligned
            if(TriangleUtils.getTriangleType(t) != TriangleType.SCALENE)
                return false;

            // A Triangle could be in 3 positions
            // Take always the smaller height of the Triangle because
            // if Spheres are aligned or close to be aligned then the smaller height
            // of the Triangle must be taken
            double height = TriangleUtils.getScaleneTriangleSmallerHeight(t);

            // If the height of the Triangle is less than the radius
            // of any of the Spheres, then there is a line traspassing them
            aligned &= height <= s1.getRadius() &&
                    height <= s2.getRadius() &&
                    height <= s3.getRadius();
        }

        return aligned;
    }

}
