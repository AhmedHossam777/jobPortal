package com.ahmed.learning.jobportal.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JobPortalSecurityConfig {
	@Qualifier("publicPaths")
	private final List<String> publicPaths;

	@Qualifier("securedPaths")
	private final List<String> securedPaths;

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {
		return http
						.csrf(AbstractHttpConfigurer::disable)
						.cors(corsConfig -> corsConfig.configurationSource(corsConfigurationSource()))
						.authorizeHttpRequests((requests) -> {
							publicPaths.forEach(publicPath -> requests.requestMatchers(publicPath).permitAll());
							securedPaths.forEach(securedPath -> requests.requestMatchers(securedPath).authenticated());
							requests.anyRequest().denyAll();
						})
						.formLogin(AbstractHttpConfigurer::disable)
						.httpBasic(withDefaults())
						.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:5173"));
		config.setAllowedMethods(Collections.singletonList("*"));
		config.setAllowedHeaders(Collections.singletonList("*"));
		config.setAllowCredentials(true);
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
