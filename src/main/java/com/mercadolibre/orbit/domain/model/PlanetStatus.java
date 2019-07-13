package com.mercadolibre.orbit.domain.model;

import com.mercadolibre.orbit.domain.enums.WeatherStatus;
import com.mercadolibre.orbit.domain.model.geometry.Point;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "planets_status")
public class PlanetStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Date date;

    @ManyToOne
    private Planet planet;

    @ManyToOne
    @JoinColumn(name="point_id", nullable=false)
    private Point position;

    @Enumerated(EnumType.STRING)
    @NotNull
    private WeatherStatus weatherStatus;

    private Double weatherIntensity;

}
