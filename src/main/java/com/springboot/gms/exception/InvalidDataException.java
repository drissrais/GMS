package com.springboot.gms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidDataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -594373371403579881L;

	public InvalidDataException(String message) {
		super(message);
	}

}
