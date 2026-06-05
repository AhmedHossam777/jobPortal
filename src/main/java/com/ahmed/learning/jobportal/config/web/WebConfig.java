package com.ahmed.learning.jobportal.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void configureApiVersioning(ApiVersionConfigurer configurer) {
		configurer
						.useMediaTypeParameter(MediaType.parseMediaType("application/vnd" +
										".dodApp+json"), "v")
						.addSupportedVersions("1.0", "2.0", "3.0").setDefaultVersion("1" +
										".0");
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		// this lambda expression usage is to know or filter controllers that
		// should have that /api prefix
		configurer.addPathPrefix("/api", _ -> true);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
						.addMapping("/api/**")
						.allowedOrigins("http://localhost:4200", "http://localhost:3000")
						.allowedMethods("*").allowedHeaders("*").exposedHeaders("*")
						.allowCredentials(true).maxAge(3600);
	}
}
