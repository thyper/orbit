package com.mercadolibre.orbit.app.job;

import com.mercadolibre.orbit.app.job.exception.OrbitCalculationJobRunnerException;
import com.mercadolibre.orbit.domain.enums.JobStatus;
import com.mercadolibre.orbit.domain.model.OrbitCalculationJob;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.service.OrbitCalculationJobService;
import com.mercadolibre.orbit.domain.service.OrbitCalculationService;
import com.mercadolibre.orbit.domain.service.PlanetService;
import com.mercadolibre.orbit.domain.service.SolarSystemService;
import com.mercadolibre.orbit.domain.service.exception.AmountOfPlanetsStatusException;
import com.mercadolibre.orbit.domain.service.exception.InsufficientPlanetsException;
import com.mercadolibre.orbit.domain.service.exception.SolarSystemNotFound;
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


    /**
     * This Scheduled trigger run by cron
     * Creates the async Job and return it
     *
     * @return
     */
    @Scheduled(cron = "0 0 12 * * *")
    public OrbitCalculationJob calculateOrbitStatus() throws OrbitCalculationJobRunnerException {

        // Get last SUCCESS Job
        OrbitCalculationJob ljob = orbitCalculationJobService.getLast(JobStatus.SUCCESS);

        // Check if there are days still not calculated between last SUCCESS Job Date and today
        // and if there is no Job still running (CREATED)
        int daysNotChecked = 0;

        if(ljob != null) {
            daysNotChecked = getDaysDifference(new Date(), ljob.getCreationDate());

            if(daysNotChecked <= 0) {
                throw new OrbitCalculationJobRunnerException(String.format(
                        "Orbit already updated by last Job (%s)",
                        ljob.getId()
                ));
            }

            if(ljob.getJobStatus().equals(JobStatus.CREATED)) {
                throw new OrbitCalculationJobRunnerException(String.format(
                        "Still a Job (%s) executing since: %s",
                        ljob.getId(),
                        ljob.getCreationDate().toString()
                ));
            }
        }

        // Creates new Job & persist it
        OrbitCalculationJob job = orbitCalculationJobService.create();
        calculateOrbitStatus(job, daysNotChecked);  // Async Task

        return job;
    }


    /**
     * Calls services that spin the Solar System & calculate new Weather of Planets
     *
     * @param job
     * @param daysNotChecked
     */
    @Async
    private void calculateOrbitStatus(OrbitCalculationJob job, int daysNotChecked) {
        // Get all Solar Systems to spin
        List<SolarSystem> solarSystems = solarSystemService.getAll();

        // Spin every Solar Systems
        for(SolarSystem solarSystem : solarSystems) {
            try {
                orbitCalculationService.spinSolarSystem(solarSystem, daysNotChecked);   // Transactional
                job.setJobStatus(orbitCalculationJobService.sumJobStatus(job.getJobStatus(), JobStatus.SUCCESS));
            } catch (InsufficientPlanetsException e) {
                job.setJobStatus(orbitCalculationJobService.sumJobStatus(job.getJobStatus(), JobStatus.FAILED));
            } catch (SolarSystemNotFound solarSystemNotFound) {
                job.setJobStatus(orbitCalculationJobService.sumJobStatus(job.getJobStatus(), JobStatus.FAILED));
            } catch (AmountOfPlanetsStatusException e) {
                job.setJobStatus(orbitCalculationJobService.sumJobStatus(job.getJobStatus(), JobStatus.FAILED));
            }
        }
    }







    /*
    Private Declarations for local use
     */

    private int getDaysDifference(Date di, Date df) {
        return Math.round(DAYS.between(dateToLocalDate(di), dateToLocalDate(df)));
    }

    private LocalDate dateToLocalDate(Date date) {
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return localDateTime.toLocalDate();
    }

}
