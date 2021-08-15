package com.novopay.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class TransactionFailedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TransactionFailedException(String message) {
		super(message);
	}
}