package com.electronistore.exception;


import com.electronistore.payload.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // TODO: 05-10-2024  ResourceNotFoundException Handler 
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> handleResourceNotFoundExceptionHandler(ResourceNotFoundException ex){

        logger.info("Exception Handler invoked");
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message(ex.getMessage())                       //-> ye message set karega
                .status(HttpStatus.NOT_FOUND)                   //-> ye status code set karega
                .success(true)                                  //-> ye success ko true ya false karega
                .build();                                       //-> ye sare data ko JSON ke form mein send karega

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

//// TODO: 05-10-2024 MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        List<ObjectError> allError = ex.getBindingResult().getAllErrors(); //-> yahan se sabhi error aa jayenge
        Map<String,Object> response = new HashMap<>();  // New Map is created for storing error new key and value pair for JSON Format

        allError.forEach(error -> {
            String message = error.getDefaultMessage();   //-> THis will give value in message form
            String field = ((FieldError)error).getField(); //-> this will give key in field form. first we change type to FieldError type

            response.put(field,message);
        });

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //// TODO: 05-10-2024  DataIntegrityViolationException
    //-> Isko bhi handle karna hai


    // TODO: 05-10-2024  BadRequestException Handler 
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponseMessage> handleBadRequestException(BadRequestException ex){

        logger.info("Bad Request Exception invoked");
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message(ex.getMessage())                       //-> ye message set karega
                .status(HttpStatus.BAD_REQUEST)                   //-> ye status code set karega
                .success(false)                                  //-> ye success ko true ya false karega
                .build();                                       //-> ye sare data ko JSON ke form mein send karega

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
