package com.mercadolibre.orbit.app.api.mapper;

import com.mercadolibre.orbit.app.api.response.PlanetStatusResponse;
import com.mercadolibre.orbit.domain.model.jpa.PlanetStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public abstract class PlanetStatusMapper {

    @Mappings({
            @Mapping(source = "source.planet", target = "planet"),
            @Mapping(ignore = true, target = "planet.solarSystem"),
            @Mapping(source = "source.date", target = "date"),
            @Mapping(source = "source.positionX", target = "positionX"),
            @Mapping(source = "source.weatherStatus", target = "weatherStatus"),
            @Mapping(source = "source.weatherIntensity", target = "weatherIntensity")
    })
    public abstract PlanetStatusResponse planetStatusToPlanetStatusResponse(PlanetStatus source);

}
