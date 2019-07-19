package com.mercadolibre.orbit.domain.service;


import com.mercadolibre.orbit.domain.model.OrbitCalculationJob;



public interface OrbitCalculationJobService {

    OrbitCalculationJob get(Long id);
    OrbitCalculationJob create(OrbitCalculationJob orbitCalculationJob);

}
