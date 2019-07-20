package com.mercadolibre.orbit.app.api.mapper;


import com.mercadolibre.orbit.app.api.request.PostPlanetRequest;
import com.mercadolibre.orbit.domain.model.Planet;
import com.mercadolibre.orbit.domain.model.SolarSystem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;




@Mapper
public abstract class PlanetMapper {

    public Planet postPlanetRequestToPlanet(PostPlanetRequest postPlanetRequest, SolarSystem solarSystem) {
        Planet planet = this.postPlanetRequestToPlanet(postPlanetRequest);
        planet.setSolarSystem(solarSystem);
        return planet;
    }


    @Mappings({
            @Mapping(target="name", source="req.name"),
            @Mapping(target="sunDistance", source="req.sunDistance"),
            @Mapping(target="degreesPerDay", source="req.degreesPerDay"),
            @Mapping(target="rotationDirection", source="req.rotationDirection")
    })
    public abstract Planet postPlanetRequestToPlanet(PostPlanetRequest req);

}
