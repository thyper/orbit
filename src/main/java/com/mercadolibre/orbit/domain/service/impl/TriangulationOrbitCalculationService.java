package com.mercadolibre.orbit.domain.service.impl;

import com.mercadolibre.orbit.domain.enums.TriangleType;
import com.mercadolibre.orbit.domain.model.transients.Point;
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
     * @param planetsPositions
     * @return
     * @throws InsufficientPlanetsException
     */
    @Override
    public boolean areAligned(List<Point> planetsPositions) throws InsufficientPlanetsPositionException {

        if(planetsPositions.size() < 3)
            throw new InsufficientPlanetsPositionException(3, planetsPositions.size());

        boolean aligned = true;

        for(int i = 2; i < planetsPositions.size(); i++) {

            Point p1 = planetsPositions.get(i);
            Point p2 = planetsPositions.get(i -1);
            Point p3 = planetsPositions.get(i - 2);

            Triangle t = new Triangle(p1, p2, p3);

            // If any of the Planets form a triangle that is not a Scalene
            // then they are not aligned
            // TEMPORAL SOLUTION !! Planet radius must replace this sentence
            if(TriangleUtils.getTriangleType(t) != TriangleType.SCALENE)
                return false;

            double height = TriangleUtils.getScaleneTriangleSmallerHeight(t);

            aligned &= height >= 0 && height <= 200;
        }

        return aligned;
    }

}
