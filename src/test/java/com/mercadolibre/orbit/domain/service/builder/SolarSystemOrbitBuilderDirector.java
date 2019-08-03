package com.mercadolibre.orbit.domain.service.builder;


public class SolarSystemOrbitBuilderDirector {

    private SolarSystemOrbitBuilder builder;

    public SolarSystemOrbitBuilderDirector(SolarSystemOrbitBuilder builder) {
        this.builder = builder;
    }

    public void build() {
        builder.reset();

        builder.setSolarSystem()
                .setPlanets()
                .setPlanetsStatuses();
    }






    /*
    Getters & Setters
     */

    public SolarSystemOrbitBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(SolarSystemOrbitBuilder builder) {
        this.builder = builder;
    }

}
