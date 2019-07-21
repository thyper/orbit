package com.mercadolibre.orbit.domain.service.exception;

public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException(Class<?> rclass, Long id) {
        super(String.format("%s with ID- %s was not found", rclass.getName(), id));
    }

    public ResourceNotFoundException(Class<?> rclass, String resourceName) {
        super(String.format("%s with name '%s' was not found", rclass.getName(), resourceName));
    }

}
