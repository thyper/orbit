package com.mercadolibre.orbit.domain.service;

import com.mercadolibre.orbit.domain.repository.SolarSystemRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SolarSystemTest extends GenericTest {

    private final static Logger logger = LoggerFactory.getLogger(SolarSystemTest.class);

    @Autowired
    private SolarSystemRepository solarSystemRepository;

    @Test
    public void testFetchSolarSystemWeatherStatus() {
    }

}
