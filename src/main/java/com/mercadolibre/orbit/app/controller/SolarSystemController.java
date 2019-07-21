package com.mercadolibre.orbit.app.controller;


import com.mercadolibre.orbit.app.api.request.PostSolarSystemRequest;
import com.mercadolibre.orbit.app.api.request.PatchSolarSystemRequest;
import com.mercadolibre.orbit.app.api.response.ApiError;
import com.mercadolibre.orbit.app.api.mapper.SolarSystemMapper;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.service.SolarSystemService;
import com.mercadolibre.orbit.domain.service.exception.ResourceNotFoundException;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/solarsystem")
public class SolarSystemController {

    @Autowired
    private SolarSystemService solarSystemService;

    private SolarSystemMapper solarSystemMapper = Mappers.getMapper(SolarSystemMapper.class);






    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id) {
        SolarSystem solarSystem = null;

        try {
            solarSystem = solarSystemService.findById(id);
        } catch (ResourceNotFoundException e) {
            ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
                    "Solar System not found",
                    e.getMessage());
            return new ResponseEntity<>(apiError, apiError.getStatus());
        }

        return new ResponseEntity<>(solarSystem, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PostSolarSystemRequest postSolarSystemRequest) {

        SolarSystem solarSystem = solarSystemService.createSolarSystem(
                solarSystemMapper.postSolarSystemRequestToSolarSystem(postSolarSystemRequest));

        return new ResponseEntity<>(solarSystem, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        solarSystemService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> patch(@PathVariable("id") Long id, @RequestBody PatchSolarSystemRequest patchSolarSystemRequest) {

        SolarSystem solarSystem = null;
        try {
            solarSystem = solarSystemService.findById(id);
            solarSystem = solarSystemMapper.patchSolarSystemRequestToSolarSystem(solarSystem, patchSolarSystemRequest);
            solarSystem = solarSystemService.save(solarSystem);
        } catch (ResourceNotFoundException e) {
            ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
                    "Solar System not found",
                    e.getMessage());
            return new ResponseEntity<>(apiError, apiError.getStatus());
        }

        return new ResponseEntity<>(solarSystem, HttpStatus.CREATED);
    }


}
