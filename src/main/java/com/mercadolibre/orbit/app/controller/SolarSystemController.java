package com.mercadolibre.orbit.app.controller;


import com.mercadolibre.orbit.app.api.ApiError;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.service.SolarSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/solarsystem")
public class SolarSystemController {

    @Autowired
    private SolarSystemService solarSystemService;


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
    public ResponseEntity<?> create(@RequestBody SolarSystem solarSystem) {
        SolarSystem solarSystem1 = solarSystemService.createSolarSystem(solarSystem);

        return new ResponseEntity<>(solarSystem1, HttpStatus.CREATED);
    }

    @DeleteMapping("id")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        solarSystemService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
