package com.mercadolibre.orbit.app.controller;


import com.mercadolibre.orbit.app.api.response.ApiError;
import com.mercadolibre.orbit.app.job.OrbitCalculationJobRunner;
import com.mercadolibre.orbit.domain.model.jpa.OrbitCalculationJob;
import com.mercadolibre.orbit.domain.service.OrbitCalculationJobService;
import com.mercadolibre.orbit.domain.service.exception.ResourceNotFoundException;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "GET the Job status by it's ID")
    public ResponseEntity<?> getJobStatus(@PathVariable("id") Long id) throws ResourceNotFoundException {

        OrbitCalculationJob job = orbitCalculationJobService.findById(id);

        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    /**
     * Run OrbitCalculationJobRunner and return Job id for later status consult
     *
     * @return
     */
    @PostMapping
    @ApiOperation(value = "POST new Job for Weather calculation. Adynchronous request")
    public ResponseEntity<?> runJob() {
        // Creates the Job
        OrbitCalculationJob job = orbitCalculationJobService.create();

        // Async Task
        orbitCalculationJobRunner.asyncTaskCalculateOrbitStatus(job);

        // Return Job while task still running for later Job status consult
        return new ResponseEntity<>(job, HttpStatus.ACCEPTED);
    }

}
