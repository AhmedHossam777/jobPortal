package com.ahmed.learning.jobportal.constants;

public class ApplicationConstants {
	public static final String JWT_SECRET_KEY = "JWT_SECRET";
	// Must be at least 32 bytes (256 bits) for HMAC-SHA — see RFC 7518 Section 3.2.
	// Development fallback only; override with the JWT_SECRET environment variable.
	public static final String JWT_DEFAULT_VALUE = "ahmeddada111_dev_only_secret_key_change_me";

	private ApplicationConstants() {
		throw new AssertionError("Cannot instantiate ApplicationConstants");
	}

}
