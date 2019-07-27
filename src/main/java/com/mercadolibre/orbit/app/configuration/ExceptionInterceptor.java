package com.mercadolibre.orbit.app.configuration;


import com.mercadolibre.orbit.app.api.response.ApiError;
import com.mercadolibre.orbit.domain.service.exception.ResourceNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;



@ControllerAdvice
public class ExceptionInterceptor {

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiError> handleContentNotAllowedException(EmptyResultDataAccessException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
                "Resource not found",
                errors);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }



    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());

        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
                String.format("%s not found", ex.getRequiredResource().getName()),
                errors);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
