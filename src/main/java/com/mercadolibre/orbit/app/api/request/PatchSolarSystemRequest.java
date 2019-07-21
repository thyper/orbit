package com.mercadolibre.orbit.app.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PatchSolarSystemRequest {

    @JsonProperty("name")
    private String name;

    /*
    Getters & Setters
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
