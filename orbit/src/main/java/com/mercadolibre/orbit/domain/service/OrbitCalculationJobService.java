package com.mercadolibre.orbit.domain.service;


import com.mercadolibre.orbit.domain.enums.JobStatus;
import com.mercadolibre.orbit.domain.model.jpa.OrbitCalculationJob;
import com.mercadolibre.orbit.domain.service.exception.ResourceNotFoundException;


public interface OrbitCalculationJobService {

    OrbitCalculationJob create();
    OrbitCalculationJob save(OrbitCalculationJob orbitCalculationJob) throws ResourceNotFoundException;
    OrbitCalculationJob findById(Long id) throws ResourceNotFoundException;
    boolean existsById(Long id);
    void deleteById(Long id);

    int countJobsByStatus(JobStatus jobStatus);
    OrbitCalculationJob getLast(JobStatus jobStatus) throws ResourceNotFoundException;
    
}
