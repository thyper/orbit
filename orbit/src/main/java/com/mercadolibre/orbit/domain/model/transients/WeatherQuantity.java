package com.mercadolibre.orbit.domain.model.transients;

import com.mercadolibre.orbit.domain.enums.WeatherStatus;

public class WeatherQuantity {

    private WeatherStatus weatherStatus;
    private Long quantity;

    public WeatherQuantity(WeatherStatus weatherStatus, Long quantity) {
        this.weatherStatus = weatherStatus;
        this.quantity = quantity;
    }


    public WeatherStatus getWeatherStatus() {
        return weatherStatus;
    }

    public void setWeatherStatus(WeatherStatus weatherStatus) {
        this.weatherStatus = weatherStatus;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
