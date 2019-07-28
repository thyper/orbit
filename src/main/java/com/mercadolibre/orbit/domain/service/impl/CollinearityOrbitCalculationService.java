package com.mercadolibre.orbit.domain.service.impl;


import com.mercadolibre.orbit.domain.model.transients.Point;
import com.mercadolibre.orbit.domain.model.transients.Sphere;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsException;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsPositionException;
import com.mercadolibre.orbit.domain.service.util.GeometryUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollinearityOrbitCalculationService extends AbstractOrbitCalculationServiceImpl {

    /**
     * Check if all planets positions in a given list are aligned
     *
     * @param planetsPositions
     * @return
     * @throws InsufficientPlanetsException
     */
    @Override
    public boolean areAligned(List<Sphere> planetsPositions) throws InsufficientPlanetsPositionException {

        if(planetsPositions.size() < 3)
            throw new InsufficientPlanetsPositionException(3, planetsPositions.size());

        boolean aligned = true;

        for(int i = 2; i < planetsPositions.size(); i++) {

            Point p1 = planetsPositions.get(i);
            Point p2 = planetsPositions.get(i -1);
            Point p3 = planetsPositions.get(i - 2);

            aligned &= GeometryUtils.areCollinear(p1, p2, p3);
        }

        return aligned;
    }

}
