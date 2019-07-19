package com.mercadolibre.orbit.app.controller;


import com.mercadolibre.orbit.app.api.response.ApiError;
import com.mercadolibre.orbit.app.api.mapper.SolarSystemMapper;
import com.mercadolibre.orbit.app.api.request.PostSolarSystemRequest;
import com.mercadolibre.orbit.app.job.OrbitCalculationJobRunner;
import com.mercadolibre.orbit.domain.model.OrbitCalculationJob;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.service.OrbitCalculationJobService;
import com.mercadolibre.orbit.domain.service.SolarSystemService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;


@RestController
@RequestMapping("/solarsystem")
public class SolarSystemController {

    @Autowired
    private SolarSystemService solarSystemService;

    @Autowired
    private OrbitCalculationJobRunner orbitCalculationJobRunner;

    @Autowired
    private OrbitCalculationJobService orbitCalculationJobService;

    private SolarSystemMapper solarSystemMapper = Mappers.getMapper(SolarSystemMapper.class);






    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id) {
        SolarSystem solarSystem = solarSystemService.findById(id);

        if(solarSystem == null) {
            ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
                    "Solar System not found",
                    "Solar System not registered");

            return new ResponseEntity<>(apiError, apiError.getStatus());
        }

        return new ResponseEntity<>(solarSystem, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PostSolarSystemRequest postSolarSystemRequest) {

        if(solarSystemMapper == null)
            return new ResponseEntity<>("NULL POINTER", HttpStatus.CONFLICT);

        SolarSystem solarSystem1 = solarSystemService.createSolarSystem(
                solarSystemMapper.postSolarSystemRequestToSolarSystem(postSolarSystemRequest));

        return new ResponseEntity<>(solarSystem1, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        solarSystemService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }




    /**
     * Run OrbitCalculationJobRunner
     *
     * @return
     */
    @GetMapping("job/{id}")
    public ResponseEntity<?> getJobStatus(@PathVariable("id") Long id) {
        OrbitCalculationJob job = orbitCalculationJobService.get(id);

        if(job == null) {
            ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
                    "Job not found",
                    "There is no Job registered with that id");
            return new ResponseEntity<>(apiError, apiError.getStatus());
        }

        return new ResponseEntity<>(job, HttpStatus.CREATED);
    }

    /**
     * Run OrbitCalculationJobRunner and return Job id for later status consult
     *
     * @return
     */
    @PostMapping("job")
    public ResponseEntity<?> runJob() {
        OrbitCalculationJob job = orbitCalculationJobRunner.calculateOrbitStatus();
        return new ResponseEntity<>(job, HttpStatus.ACCEPTED);
    }

}
