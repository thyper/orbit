package com.mercadolibre.orbit.app.api.mapper;

import com.mercadolibre.orbit.app.api.request.PostSolarSystemRequest;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;





@Mapper
public abstract class SolarSystemMapper {

    @Mappings({
            @Mapping(target="name", source="req.name"),
            @Mapping(target="posX", source="req.posX"),
            @Mapping(target="posY", source="req.posY")
    })
    public abstract SolarSystem postSolarSystemRequestToSolarSystem(PostSolarSystemRequest req);

}
