package com.mercadolibre.orbit.app.controller;


import com.mercadolibre.orbit.app.api.mapper.PlanetStatusMapper;
import com.mercadolibre.orbit.app.api.request.PostSolarSystemRequest;
import com.mercadolibre.orbit.app.api.request.PatchSolarSystemRequest;
import com.mercadolibre.orbit.app.api.response.ApiError;
import com.mercadolibre.orbit.app.api.mapper.SolarSystemMapper;
import com.mercadolibre.orbit.app.api.response.PlanetStatusResponse;
import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;
import com.mercadolibre.orbit.domain.model.transients.WeatherQuantity;
import com.mercadolibre.orbit.domain.service.SolarSystemService;
import com.mercadolibre.orbit.domain.service.WeatherService;
import com.mercadolibre.orbit.domain.service.exception.ResourceNotFoundException;
import io.swagger.annotations.ApiOperation;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/solarsystem")
public class SolarSystemController {

    @Autowired
    private SolarSystemService solarSystemService;

    @Autowired
    private WeatherService weatherService;


    private SolarSystemMapper solarSystemMapper = Mappers.getMapper(SolarSystemMapper.class);
    private PlanetStatusMapper planetStatusMapper = Mappers.getMapper(PlanetStatusMapper.class) ;




    @GetMapping("")
    @ApiOperation(value = "GET all Solar Systems")
    public ResponseEntity<?> get() {
        return new ResponseEntity<>(solarSystemService.getAll(), HttpStatus.OK);
    }


    @GetMapping("{id}")
    @ApiOperation(value = "GET a SolarSystem by ID")
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
    @ApiOperation(value = "POST new SolarSystem")
    public ResponseEntity<?> create(@RequestBody PostSolarSystemRequest postSolarSystemRequest) {

        SolarSystem solarSystem = solarSystemService.createSolarSystem(
                solarSystemMapper.postSolarSystemRequestToSolarSystem(postSolarSystemRequest));

        return new ResponseEntity<>(solarSystem, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete a SolarSystem by ID")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        solarSystemService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("{id}")
    @ApiOperation(value = "PATCH a SolarSystem")
    public ResponseEntity<?> patch(@PathVariable("id") Long id,
                                   @RequestBody PatchSolarSystemRequest patchSolarSystemRequest) throws ResourceNotFoundException {

        SolarSystem solarSystem = solarSystemService.findById(id);
        solarSystem = solarSystemMapper.patchSolarSystemRequestToSolarSystem(solarSystem, patchSolarSystemRequest);
        solarSystem = solarSystemService.save(solarSystem);

        return new ResponseEntity<>(solarSystem, HttpStatus.CREATED);
    }




    /**
     * Consults Planets Statuses of a Solar System in a determinated Date
     *
     * @param solarSystemId
     * @param date
     * @return
     */
    @GetMapping("{id}/weather/{date}")
    @ApiOperation(value = "Get Planets Status of a Solar System by it's ID and a specific date (yyyy-mm-dd)")
    public ResponseEntity<?> getSolarSystemStatus(@PathVariable("id") Long solarSystemId,
                                                  @PathVariable("date")
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) throws ResourceNotFoundException {

        SolarSystem solarSystem = solarSystemService.findById(solarSystemId);
        List<PlanetStatus> planetStatuses = solarSystemService.getSolarSystemStatus(solarSystem, date);

        List<PlanetStatusResponse> pss = new ArrayList<>();
        for(PlanetStatus ps : planetStatuses) {
            pss.add(planetStatusMapper.planetStatusToPlanetStatusResponse(ps));
        }

        return new ResponseEntity<>(pss, HttpStatus.OK);
    }

}