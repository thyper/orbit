package com.mercadolibre.orbit.app.api.mapper;

import com.mercadolibre.orbit.app.api.request.CoarsePostSolarSystemRequest;
import com.mercadolibre.orbit.app.api.request.PostSolarSystemRequest;
import com.mercadolibre.orbit.app.api.request.PatchSolarSystemRequest;
import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;
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
            @Mapping(target="posY", source="req.posY"),
            @Mapping(target="sunRadius", source="req.sunRadius")
    })
    public abstract SolarSystem postSolarSystemRequestToSolarSystem(PostSolarSystemRequest req);


    @Mappings({
            @Mapping(target="name", source="req.solarSystemRequest.name"),
            @Mapping(target="posX", source="req.solarSystemRequest.posX"),
            @Mapping(target="posY", source="req.solarSystemRequest.posY"),
            @Mapping(target="sunRadius", source="req.solarSystemRequest.sunRadius")
    })
    public abstract SolarSystem extractSolarSystem(CoarsePostSolarSystemRequest req);

}
