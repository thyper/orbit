package com.mercadolibre.orbit.app.controller;

import com.mercadolibre.orbit.domain.model.transients.WeatherQuantity;
import com.mercadolibre.orbit.domain.service.WeatherService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;


    @GetMapping("pronostics/{date}")
    @ApiOperation(value = "Get Weather pronostics by a started date (yyyy-mm-dd)")
    public ResponseEntity<?> getWeatherPronostics(@PathVariable("date")
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

        List<WeatherQuantity> pronostics = weatherService.fetchWeatherPronosticsSinceDate(date);

        return new ResponseEntity<>(pronostics, HttpStatus.OK);
    }

}
