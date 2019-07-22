package com.mercadolibre.orbit.app.job;

import com.mercadolibre.orbit.app.job.exception.JobRuntimeException;
import com.mercadolibre.orbit.app.job.exception.JobStillRunningRuntimeException;
import com.mercadolibre.orbit.app.util.DateUtil;
import com.mercadolibre.orbit.domain.enums.JobStatus;
import com.mercadolibre.orbit.domain.enums.SpiningStatus;
import com.mercadolibre.orbit.domain.model.jpa.OrbitCalculationJob;
import com.mercadolibre.orbit.domain.service.OrbitCalculationJobService;
import com.mercadolibre.orbit.domain.service.OrbitCalculationService;
import com.mercadolibre.orbit.domain.service.PlanetService;
import com.mercadolibre.orbit.domain.service.SolarSystemService;
import com.mercadolibre.orbit.domain.service.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;



@Component
public class OrbitCalculationJobRunner {

    @Value("${years_to_pronostic}")
    private int yearsToPronostic;

    @Autowired
    private SolarSystemService solarSystemService;

    @Autowired
    private PlanetService planetService;

    @Autowired
    private OrbitCalculationService orbitCalculationService;

    @Autowired
    private OrbitCalculationJobService orbitCalculationJobService;

    private Logger logger = LoggerFactory.getLogger(OrbitCalculationJobRunner.class);


    /**
     * This Scheduled trigger run by cron
     */
    @Scheduled(cron = "0 0 12 * * *")
    public void runJob() {
        OrbitCalculationJob job = orbitCalculationJobService.create();
        asyncTaskCalculateOrbitStatus(job);
    }

    /**
     * Calls services that spin the Solar System & calculate new Weather of Planets
     *
     * @return
     */
    //@Scheduled(cron = "0 0 12 * * *")
    @Async("processExecutor")
    public void asyncTaskCalculateOrbitStatus(OrbitCalculationJob job) throws JobRuntimeException, JobStillRunningRuntimeException {
        // Check if there is no Job still running (JobStatus.CREATED)
        OrbitCalculationJob lJobRunning = null;
        try {
            lJobRunning = orbitCalculationJobService.getLast(JobStatus.ONGOING);
        } catch (ResourceNotFoundException e) {
            logger.info("No ONGOING Job registered. Proceeding with this one");
        }

        if(lJobRunning != null) {
            // Kill Job and save it
            job.setJobStatus(JobStatus.TERMINATED);
            try {
                orbitCalculationJobService.save(job);
            } catch (ResourceNotFoundException e) {
                String msg = "Breaking Job. Reasons: " + e.getMessage();
                logger.error(msg);
                throw new JobRuntimeException(msg);
            }

            throw new JobStillRunningRuntimeException(String.format(
                    "Can't proceed with Job because there is still a Job '%s' running since %s",
                    lJobRunning.getId(),
                    lJobRunning.getCreationDate().toString()
            ));
        }

        // Set Job as ONGOING status
        job.setJobStatus(JobStatus.ONGOING);
        try {
            orbitCalculationJobService.save(job);
        } catch (ResourceNotFoundException e) {
            String msg = "Couldn't set new ONGOING status for Job. Breaking task. REASONS: " + e.getMessage();
            logger.error(msg);
            throw new JobRuntimeException(msg);
        }

        // Spin Solar Systems to a specific Date
        final Date yearsLater = DateUtil.sumDays(new Date(), 365 * this.yearsToPronostic);
        SpiningStatus spiningStatus = orbitCalculationService.spinSolarSystems(yearsLater);

        if(spiningStatus.equals(SpiningStatus.OK))
            job.setJobStatus(JobStatus.SUCCESS);
        else
            job.setJobStatus(JobStatus.FAILED);

        // Save Job
        try {
            orbitCalculationJobService.save(job);
        } catch (ResourceNotFoundException e) {
            String msg = "Couldn't finish and save Job. Reasons: " + e.getMessage();
            logger.error(msg);
            throw new JobRuntimeException(msg);
        }
    }

}
