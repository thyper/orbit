package com.mercadolibre.orbit.app.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercadolibre.orbit.domain.enums.ClockDirection;




public class PostPlanetRequest extends PostOrphanPlanetRequest {


    @JsonProperty("solar_system_id")
    private Long solarSystemId;





    /*
    Getters & Setters
     */

    public Long getSolarSystemId() {
        return solarSystemId;
    }

    public void setSolarSystemId(Long solarSystemId) {
        this.solarSystemId = solarSystemId;
    }
}
