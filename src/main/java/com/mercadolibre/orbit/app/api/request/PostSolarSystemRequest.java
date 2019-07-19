package com.mercadolibre.orbit.app.api.request;


import com.fasterxml.jackson.annotation.JsonProperty;

public class PostSolarSystemRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("pos_x")
    private Double posX;

    @JsonProperty("pos_y")
    private Double posY;




    /*
    Getters & Setters
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPosX() {
        return posX;
    }

    public void setPosX(Double posX) {
        this.posX = posX;
    }

    public Double getPosY() {
        return posY;
    }

    public void setPosY(Double posY) {
        this.posY = posY;
    }
}
