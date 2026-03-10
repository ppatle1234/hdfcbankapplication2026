package com.fullstack.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalCustomExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleCustomException(RecordNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleInsufficientFundException(InsufficientFundException exception){
        return new ResponseEntity<>("{errors}" + exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleCustomValidation(MethodArgumentNotValidException exception){

        Map<String, String> errors = new LinkedHashMap<>();

         exception.getBindingResult().getAllErrors().forEach(objectError -> {
             String field = ((FieldError)objectError).getField();
             String fieldMessage = objectError.getDefaultMessage();

             errors.put(field, fieldMessage);
         });

         return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
