package com.mercadolibre.orbit.app.controller;


import com.mercadolibre.orbit.app.api.response.ApiError;
import com.mercadolibre.orbit.app.job.OrbitCalculationJobRunner;
import com.mercadolibre.orbit.domain.model.OrbitCalculationJob;
import com.mercadolibre.orbit.domain.service.OrbitCalculationJobService;
import com.mercadolibre.orbit.domain.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/job")
public class OrbitCalculationJobController {

    @Autowired
    private OrbitCalculationJobService orbitCalculationJobService;

    @Autowired
    private OrbitCalculationJobRunner orbitCalculationJobRunner;

    /**
     * Run OrbitCalculationJobRunner
     *
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<?> getJobStatus(@PathVariable("id") Long id) {

        OrbitCalculationJob job = null;
        try {
            job = orbitCalculationJobService.findById(id);
        } catch (ResourceNotFoundException e) {
            ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
                    "Job not found",
                    e.getMessage());
            return new ResponseEntity<>(apiError, apiError.getStatus());
        }

        return new ResponseEntity<>(job, HttpStatus.CREATED);
    }

    /**
     * Run OrbitCalculationJobRunner and return Job id for later status consult
     *
     * @return
     */
    @PostMapping
    public ResponseEntity<?> runJob() {
        // Creates the Job
        OrbitCalculationJob job = orbitCalculationJobService.create();

        // Async Task
        orbitCalculationJobRunner.asyncTaskCalculateOrbitStatus(job);

        // Return Job while task still running for later Job status consult
        return new ResponseEntity<>(job, HttpStatus.ACCEPTED);
    }

}
