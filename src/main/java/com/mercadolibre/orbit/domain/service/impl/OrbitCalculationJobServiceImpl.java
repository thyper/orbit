package com.mercadolibre.orbit.domain.service.impl;


import com.mercadolibre.orbit.domain.enums.JobStatus;
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
    public OrbitCalculationJob getLast(JobStatus jobStatus) {
        return orbitCalculationJobRepository.getLast(jobStatus.toString());
    }

    @Override
    public OrbitCalculationJob create(OrbitCalculationJob orbitCalculationJob) {
        return orbitCalculationJobRepository.save(orbitCalculationJob);
    }

    @Override
    public OrbitCalculationJob create() {
        OrbitCalculationJob job = new OrbitCalculationJob();
        job.setJobStatus(JobStatus.CREATED);

        return orbitCalculationJobRepository.save(job);
    }



    @Override
    public JobStatus sumJobStatus(JobStatus actualJobStatus, JobStatus newJobStatus) {

        if(actualJobStatus.equals(JobStatus.CREATED) && !newJobStatus.equals(JobStatus.FAILED))
            return newJobStatus;

        if(actualJobStatus.equals(JobStatus.SUCCESS) && newJobStatus.equals(JobStatus.FAILED))
            return JobStatus.PARTIAL_SUCCESS;

        if(actualJobStatus.equals(JobStatus.FAILED) && !newJobStatus.equals(JobStatus.CREATED))
            return JobStatus.PARTIAL_SUCCESS;

        if(actualJobStatus.equals(JobStatus.PARTIAL_SUCCESS))
            return JobStatus.PARTIAL_SUCCESS;

        return newJobStatus;
    }

}
