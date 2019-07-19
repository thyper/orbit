package com.mercadolibre.orbit.app.controller;

import com.mercadolibre.orbit.app.api.ApiError;
import com.mercadolibre.orbit.app.api.mapper.PlanetMapper;
import com.mercadolibre.orbit.app.api.request.PostPlanetRequest;
import com.mercadolibre.orbit.domain.model.Planet;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import com.mercadolibre.orbit.domain.service.PlanetService;
import com.mercadolibre.orbit.domain.service.SolarSystemService;
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

        Planet planet = planetService.findPlanetById(id);

        if(planet == null) {
            ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
                    "Planet not found",
                    "Planet not registered");
            return new ResponseEntity<>(apiError, apiError.getStatus());
        }

        return new ResponseEntity<>(planet, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody PostPlanetRequest postPlanetRequest) {

        SolarSystem solarSystem = solarSystemService.findById(postPlanetRequest.getSolarSystemId());

        if(solarSystem == null) {
            ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
                    "Solar System not found",
                    "Solar System not registered");
            return new ResponseEntity<>(apiError, apiError.getStatus());
        }

        Planet planet = planetService.createPlanet(planetMapper.postPlanetRequestToPlanet(postPlanetRequest, solarSystem));
        return new ResponseEntity<>(planet, HttpStatus.CREATED);
    }

}
