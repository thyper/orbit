package com.mercadolibre.orbit.app.api.mapper;


import com.mercadolibre.orbit.app.api.request.PatchPlanetRequest;
import com.mercadolibre.orbit.app.api.request.PostPlanetRequest;
import com.mercadolibre.orbit.domain.model.jpa.Planet;
import com.mercadolibre.orbit.domain.model.jpa.SolarSystem;
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

    public Planet patchPlanetRequestToPlanet(Planet target,
                                             PatchPlanetRequest source) {
        Planet planet = target;
        planet.setName(source.getName() != null ? source.getName() : target.getName());
        planet.setSunDistance(source.getSunDistance() != null ? source.getSunDistance() : target.getSunDistance());
        planet.setDegreesPerDay(source.getDegreesPerDay() != null ? source.getDegreesPerDay() : target.getDegreesPerDay());
        planet.setRotationDirection(source.getRotationDirection() != null ? source.getRotationDirection() : target.getRotationDirection());

        return planet;
    }


    @Mappings({
            @Mapping(target="name", source="req.name"),
            @Mapping(target="sunDistance", source="req.sunDistance"),
            @Mapping(target="degreesPerDay", source="req.degreesPerDay"),
            @Mapping(target="rotationDirection", source="req.rotationDirection"),
            @Mapping(target="radius", source="req.radius")
    })
    public abstract Planet postPlanetRequestToPlanet(PostPlanetRequest req);

}
