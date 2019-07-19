package com.mercadolibre.orbit.domain.service;


import com.mercadolibre.orbit.app.job.OrbitCalculationJobRunner;
import com.mercadolibre.orbit.domain.model.OrbitCalculationJob;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class OrbitCalculationJobServiceTest extends GenericTest {

    @Autowired
    private OrbitCalculationJobRunner orbitCalculationJobRunner;

    @Test
    public void testJobCreation() {
        OrbitCalculationJob job = orbitCalculationJobRunner.calculateOrbitStatus();
        Assert.notNull(job);
    }


}
