package com.mercadolibre.orbit.domain.service.exception;

public class ResourceNotFoundException extends Exception {

    private Class requiredResource;

    public ResourceNotFoundException(Class rclass, Long id) {
        super(String.format("%s with ID- %s was not found", rclass.getName(), id));
        this.requiredResource = rclass;
    }

    public ResourceNotFoundException(Class rclass, String resourceName) {
        super(String.format("%s with name '%s' was not found", rclass.getName(), resourceName));
        this.requiredResource = rclass;
    }



    public Class getRequiredResource() {
        return requiredResource;
    }

    public void setRequiredResource(Class requiredResource) {
        this.requiredResource = requiredResource;
    }
}
