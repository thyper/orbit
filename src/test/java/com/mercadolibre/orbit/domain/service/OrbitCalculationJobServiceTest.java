package com.mercadolibre.orbit.domain.service;


import com.mercadolibre.orbit.app.job.OrbitCalculationJobRunner;
import com.mercadolibre.orbit.app.job.exception.OrbitCalculationJobRunnerException;
import com.mercadolibre.orbit.domain.enums.JobStatus;
import com.mercadolibre.orbit.domain.model.OrbitCalculationJob;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Assert;

public class OrbitCalculationJobServiceTest extends GenericTest {

    @Autowired
    private OrbitCalculationJobService orbitCalculationJobService;

    @Autowired
    private OrbitCalculationJobRunner orbitCalculationJobRunner;

    @Test
    public void testJobCreation() throws OrbitCalculationJobRunnerException {
        OrbitCalculationJob job = orbitCalculationJobRunner.calculateOrbitStatus();
        Assert.assertNotNull(job);
    }

    @Test
    public void testJobSucceedCreation() {
        OrbitCalculationJob job = orbitCalculationJobService.create();

        Assert.assertNotNull(job);

        OrbitCalculationJob ljob = orbitCalculationJobService.getLast(JobStatus.CREATED);

        Assert.assertEquals(job.getId(), ljob.getId());
    }


}
