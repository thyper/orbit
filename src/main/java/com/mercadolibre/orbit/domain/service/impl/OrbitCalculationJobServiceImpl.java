package com.mercadolibre.orbit.domain.service.impl;


import com.mercadolibre.orbit.domain.enums.JobStatus;
import com.mercadolibre.orbit.domain.model.OrbitCalculationJob;
import com.mercadolibre.orbit.domain.repository.OrbitCalculationJobRepository;
import com.mercadolibre.orbit.domain.service.OrbitCalculationJobService;
import com.mercadolibre.orbit.domain.service.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrbitCalculationJobServiceImpl implements OrbitCalculationJobService {

    private Logger logger = LoggerFactory.getLogger(OrbitCalculationJobServiceImpl.class);

    @Autowired
    private OrbitCalculationJobRepository orbitCalculationJobRepository;



    @Override
    public OrbitCalculationJob create() {
        OrbitCalculationJob job = new OrbitCalculationJob();
        return orbitCalculationJobRepository.save(job);
    }


    @Override
    public OrbitCalculationJob save(OrbitCalculationJob orbitCalculationJob) throws ResourceNotFoundException {

        if(!orbitCalculationJobRepository.existsById(orbitCalculationJob.getId()))
            throw new ResourceNotFoundException(OrbitCalculationJob.class, orbitCalculationJob.getId());

        return orbitCalculationJobRepository.save(orbitCalculationJob);
    }


    @Override
    public OrbitCalculationJob findById(Long id) throws ResourceNotFoundException {

        if(!orbitCalculationJobRepository.existsById(id))
            throw new ResourceNotFoundException(OrbitCalculationJob.class, id);

        return orbitCalculationJobRepository.findById(id).orElse(null);
    }


    @Override
    public boolean existsById(Long id) {
        return orbitCalculationJobRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        orbitCalculationJobRepository.deleteById(id);
    }


    @Override
    public int countJobsByStatus(JobStatus jobStatus) {
        return orbitCalculationJobRepository.countByStatus(jobStatus);
    }


    @Override
    public OrbitCalculationJob getLast(JobStatus jobStatus) throws ResourceNotFoundException {

        List<OrbitCalculationJob> orbitCalculationJobList = orbitCalculationJobRepository.getLast(jobStatus, PageRequest.of(0, 1));

        if(orbitCalculationJobList.size() > 0)
            return orbitCalculationJobList.get(0);
        else throw new ResourceNotFoundException(JobStatus.class, jobStatus.toString());
    }

}
