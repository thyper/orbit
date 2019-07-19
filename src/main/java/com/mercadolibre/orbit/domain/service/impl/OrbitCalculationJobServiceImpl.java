package com.mercadolibre.orbit.domain.service.impl;


import com.mercadolibre.orbit.domain.model.OrbitCalculationJob;
import com.mercadolibre.orbit.domain.repository.OrbitCalculationJobRepository;
import com.mercadolibre.orbit.domain.service.OrbitCalculationJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrbitCalculationJobServiceImpl implements OrbitCalculationJobService {

    @Autowired
    private OrbitCalculationJobRepository orbitCalculationJobRepository;

    @Override
    public OrbitCalculationJob get(Long id) {
        return orbitCalculationJobRepository.findById(id).orElse(null);
    }

    @Override
    public OrbitCalculationJob create(OrbitCalculationJob orbitCalculationJob) {
        return orbitCalculationJobRepository.save(orbitCalculationJob);
    }

}
