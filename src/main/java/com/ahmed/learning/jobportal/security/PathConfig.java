package com.ahmed.learning.jobportal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PathConfig {
	@Bean(name = "publicPaths")
	public List<String> publicPaths() {
		return List.of(
						"/api/companies/public",
						"/api/auth/login/public",
						"/api/csrf-token/public",
						"/api/auth/register/public",
						"/api/contacts/public",
						"/swagger-ui.html",
						"/swagger-ui/**",
						"/v3/api-docs/**",
						"/swagger-resources/**",
						"/webjars/**"
		);
	}

	@Bean(name = "securedPaths")
	public List<String> securedPaths() {
		return List.of("/api/**");
	}
}
