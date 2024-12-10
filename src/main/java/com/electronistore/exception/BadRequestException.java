package com.electronistore.exception;


import lombok.Builder;

@Builder
public class BadRequestException extends RuntimeException{

    public BadRequestException() {
        super("Bad Request!!");
    }

    public BadRequestException(String message) {
        super(message);
    }
}
