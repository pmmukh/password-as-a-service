package com.codesample.braincorp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class NixFileNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public NixFileNotFoundException(String exception) {
		super(exception);
	}
}
