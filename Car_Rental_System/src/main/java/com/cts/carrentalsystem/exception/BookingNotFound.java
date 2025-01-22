package com.cts.carrentalsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BookingNotFound extends RuntimeException {
	
	private String message;
   public BookingNotFound(String message) {
	// TODO Auto-generated constructor stub
	   super(message);
		this.message = message;

    }
}
