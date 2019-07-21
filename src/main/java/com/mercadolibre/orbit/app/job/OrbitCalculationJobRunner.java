package com.mercadolibre.orbit.app.job;

import com.mercadolibre.orbit.app.job.exception.JobStillRunningRuntimeException;
import com.mercadolibre.orbit.app.util.DateUtil;
import com.mercadolibre.orbit.domain.enums.JobStatus;
import com.mercadolibre.orbit.domain.enums.SpiningStatus;
import com.mercadolibre.orbit.domain.model.OrbitCalculationJob;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.service.OrbitCalculationJobService;
import com.mercadolibre.orbit.domain.service.OrbitCalculationService;
import com.mercadolibre.orbit.domain.service.PlanetService;
import com.mercadolibre.orbit.domain.service.SolarSystemService;
import com.mercadolibre.orbit.domain.service.exception.AmountOfPlanetsStatusException;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsException;
import com.mercadolibre.orbit.domain.service.exception.SolarSystemNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;


@Component
public class OrbitCalculationJobRunner {

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
     * Calls services that spin the Solar System & calculate new Weather of Planets
     *
     * @return
     */
    @Scheduled(cron = "0 0 12 * * *")
    @Async("processExecutor")
    public void asyncTaskCalculateOrbitStatus(OrbitCalculationJob job) throws JobStillRunningRuntimeException {
        // Check if there is no Job still running (JobStatus.CREATED)
        OrbitCalculationJob lJobRunning = orbitCalculationJobService.getLast(JobStatus.ONGOING);
        if(lJobRunning != null) {
            // Kill Job and save it
            job.setJobStatus(JobStatus.TERMINATED);
            orbitCalculationJobService.save(job);

            throw new JobStillRunningRuntimeException(String.format(
                    "Can't proceed with Job because there is still a Job '%s' running since %s",
                    lJobRunning.getId(),
                    lJobRunning.getCreationDate().toString()
            ));
        }

        // Set Job as ONGOING status
        job.setJobStatus(JobStatus.ONGOING);
        orbitCalculationJobService.save(job);

        // Spin Solar Systems to a specific Date
        final Date tenYearsLater = DateUtil.sumDays(new Date(), 5);
        SpiningStatus spiningStatus = orbitCalculationService.spinSolarSystems(new Date());

        if(spiningStatus.equals(SpiningStatus.OK))
            job.setJobStatus(JobStatus.SUCCESS);
        else
            job.setJobStatus(JobStatus.FAILED);

        // Save Job
        orbitCalculationJobService.save(job);
    }

}
