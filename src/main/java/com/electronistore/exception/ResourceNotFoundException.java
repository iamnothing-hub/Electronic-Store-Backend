package com.electronistore.exception;

import lombok.Builder;

@Builder
public class ResourceNotFoundException extends RuntimeException {


    public ResourceNotFoundException() {
        super("Resource Not Found!! by parameterless constructor");
    }

// todo Parametrized Constructor
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
