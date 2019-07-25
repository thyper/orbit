package com.mercadolibre.orbit.app.controller;

import com.mercadolibre.orbit.app.api.request.PatchPlanetRequest;
import com.mercadolibre.orbit.app.api.response.ApiError;
import com.mercadolibre.orbit.app.api.mapper.PlanetMapper;
import com.mercadolibre.orbit.app.api.request.PostPlanetRequest;
import com.mercadolibre.orbit.domain.model.jpa.Planet;
import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;
import com.mercadolibre.orbit.domain.service.PlanetService;
import com.mercadolibre.orbit.domain.service.SolarSystemService;
import com.mercadolibre.orbit.domain.service.exception.ResourceNotFoundException;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/planet")
public class PlanetController {

    @Autowired
    private SolarSystemService solarSystemService;

    @Autowired
    private PlanetService planetService;


    private PlanetMapper planetMapper = Mappers.getMapper(PlanetMapper.class);



    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id) {

        Planet planet = null;
        try {
            planet = planetService.findPlanetById(id);
        } catch (ResourceNotFoundException e) {
            ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
                    "Planet not found",
                    e.getMessage());
            return new ResponseEntity<>(apiError, apiError.getStatus());
        }

        return new ResponseEntity<>(planet, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody PostPlanetRequest postPlanetRequest) {

        SolarSystem solarSystem = null;
        try {
            solarSystem = solarSystemService.findById(postPlanetRequest.getSolarSystemId());

            // Allow only 3 planets by SolarSystem
            if(planetService.countPlanetsBySolarSystem(solarSystem.getId()) > 3) {
                ApiError apiError = new ApiError(HttpStatus.CONFLICT,
                        "Too many Planets",
                        "Too many Planets for Solar System. Only 3 Planets are allowd by Solar System");
                return new ResponseEntity<>(apiError, apiError.getStatus());
            }
        } catch (ResourceNotFoundException e) {
            ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
                    "Solar System not found",
                    e.getMessage());
            return new ResponseEntity<>(apiError, apiError.getStatus());
        }

        Planet planet = planetService.createPlanet(planetMapper.postPlanetRequestToPlanet(postPlanetRequest, solarSystem));
        return new ResponseEntity<>(planet, HttpStatus.CREATED);
    }


    @PatchMapping("{id}")
    public ResponseEntity<?> patch(@PathVariable("id") Long id, @RequestBody PatchPlanetRequest planetRequest) {

        Planet planet = null;
        try {
            planet = planetService.findPlanetById(id);
            planet = planetMapper.patchPlanetRequestToPlanet(planet, planetRequest);
            planet = planetService.save(planet);
        } catch (ResourceNotFoundException e) {
            ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
                    "Planet not found",
                    e.getMessage());
            return new ResponseEntity<>(apiError, apiError.getStatus());
        }

        return new ResponseEntity<>(planet, HttpStatus.OK);
    }

}
