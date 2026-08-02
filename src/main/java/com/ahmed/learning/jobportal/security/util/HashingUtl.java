package com.ahmed.learning.jobportal.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class HashingUtl {
	public String hashPassword(String password) {
		var bcrypt = new BCryptPasswordEncoder();

		return bcrypt.encode(password);
	}
}
