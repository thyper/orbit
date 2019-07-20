package com.mercadolibre.orbit.domain.service;


import com.mercadolibre.orbit.domain.enums.JobStatus;
import com.mercadolibre.orbit.domain.model.OrbitCalculationJob;



public interface OrbitCalculationJobService {

    OrbitCalculationJob get(Long id);
    OrbitCalculationJob getLast(JobStatus jobStatus);
    OrbitCalculationJob save(OrbitCalculationJob orbitCalculationJob);
    OrbitCalculationJob create();
    int countJobsByStatus(JobStatus jobStatus);

}
