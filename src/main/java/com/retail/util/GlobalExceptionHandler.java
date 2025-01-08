package com.retail.util;

import com.retail.exception.InputValidationException;
import com.retail.exception.OperationFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ InputValidationException.class, OperationFailureException.class })
    public ResponseEntity<ResponsePojo> handleInputValidationException(Exception  exception) {

        ResponsePojo errorResponse = new ResponsePojo(ApplicationConstants.FAILURE,exception.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<ResponsePojo> handleMethodArgumentNotValidException(MethodArgumentNotValidException  exception) {

        Map<String,String> errors = new LinkedHashMap<>();
        exception.getFieldErrors().forEach(fieldError->{
            errors.put(fieldError.getField(),fieldError.getDefaultMessage());
        });

        ResponsePojo errorResponse = new ResponsePojo(ApplicationConstants.FAILURE,errors.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public ResponseEntity<ResponsePojo> handleHttpMessageNotReadableException(HttpMessageNotReadableException  exception) {


        ResponsePojo errorResponse = new ResponsePojo(ApplicationConstants.FAILURE,exception.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
