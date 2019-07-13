package com.mercadolibre.orbit.domain.model;

import com.mercadolibre.orbit.domain.enums.ClockDirection;
import com.mercadolibre.orbit.domain.model.geometry.Point;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "planets")
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Double sunDistance;

    @NotNull
    private Double degreesPerDay;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ClockDirection rotationDirection;
}
