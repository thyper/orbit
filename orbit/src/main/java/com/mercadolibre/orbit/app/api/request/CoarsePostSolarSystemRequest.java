package com.mercadolibre.orbit.app.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CoarsePostSolarSystemRequest {

    @JsonProperty("solar_system")
    private PostSolarSystemRequest solarSystemRequest;

    @JsonProperty("planets")
    private List<PostOrphanPlanetRequest> planetsRequest;


    /*
    Getters & Setters
     */
    public PostSolarSystemRequest getSolarSystemRequest() {
        return solarSystemRequest;
    }

    public void setSolarSystemRequest(PostSolarSystemRequest solarSystemRequest) {
        this.solarSystemRequest = solarSystemRequest;
    }

    public List<PostOrphanPlanetRequest> getPlanetsRequest() {
        return planetsRequest;
    }

    public void setPlanetsRequest(List<PostOrphanPlanetRequest> planetsRequest) {
        this.planetsRequest = planetsRequest;
    }
}
