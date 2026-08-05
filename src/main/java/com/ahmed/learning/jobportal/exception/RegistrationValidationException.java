package com.ahmed.learning.jobportal.exception;

import java.util.HashMap;

public class RegistrationValidationException extends RuntimeException {
	private final HashMap<String, String> errors;

	public RegistrationValidationException(HashMap<String, String> errors) {
		super("Registration validation failed!");
		this.errors = errors;
	}

	public HashMap<String, String> getErrors() {
		return errors;
	}
}
