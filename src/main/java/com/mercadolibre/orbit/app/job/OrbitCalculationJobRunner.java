package com.mercadolibre.orbit.app.job;

import com.mercadolibre.orbit.domain.enums.JobStatus;
import com.mercadolibre.orbit.domain.model.OrbitCalculationJob;
import com.mercadolibre.orbit.domain.service.OrbitCalculationJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



@Component
public class OrbitCalculationJobRunner {

    @Autowired
    private OrbitCalculationJobService orbitCalculationJobService;


    /**
     * This Scheduled trigger run by cron
     * Calls services that spin the Solar System & calculate new Weather of Planets
     *
     * @return
     */
    @Scheduled(cron = "0 0 12 * * *")
    public OrbitCalculationJob calculateOrbitStatus() {
        OrbitCalculationJob job = new OrbitCalculationJob();
        job.setJobStatus(JobStatus.CREATED);

        job = orbitCalculationJobService.create(job);

        // Execute Solar System Spin

        return job;
    }

}
