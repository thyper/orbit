package com.mercadolibre.orbit.app.api.mapper;

import com.mercadolibre.orbit.app.api.request.PostSolarSystemRequest;
import com.mercadolibre.orbit.app.api.request.PatchSolarSystemRequest;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;





@Mapper
public abstract class SolarSystemMapper {

    public SolarSystem patchSolarSystemRequestToSolarSystem(SolarSystem target,
                                                            PatchSolarSystemRequest source) {
        SolarSystem solarSystem = target;
        solarSystem.setName(source.getName() != null ? source.getName() : target.getName());
        return solarSystem;
    }


    @Mappings({
            @Mapping(target="name", source="req.name"),
            @Mapping(target="posX", source="req.posX"),
            @Mapping(target="posY", source="req.posY")
    })
    public abstract SolarSystem postSolarSystemRequestToSolarSystem(PostSolarSystemRequest req);

}
