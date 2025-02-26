package com.springboot.gms.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springboot.gms.exception.InvalidDataException;
import com.springboot.gms.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(InvalidDataException.class)
	public ResponseEntity<String> handleInvalidDataException(InvalidDataException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		return new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST);
	}

}
