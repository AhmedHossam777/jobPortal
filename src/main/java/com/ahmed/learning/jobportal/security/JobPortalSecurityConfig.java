package com.ahmed.learning.jobportal.security;

import com.ahmed.learning.jobportal.constants.ApplicationConstants;
import com.ahmed.learning.jobportal.security.filter.JwtTokenValidatorFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
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
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, Environment env) {
		// resolved here, not inside the filter: a filter registered with addFilterBefore is
		// not a bean, so its own getEnvironment() would not see the application properties
		String jwtSecret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
						ApplicationConstants.JWT_DEFAULT_VALUE);

		return http
						.csrf(AbstractHttpConfigurer::disable)
						.cors(corsConfig -> corsConfig.configurationSource(corsConfigurationSource()))
						.authorizeHttpRequests((requests) -> {
							publicPaths.forEach(publicPath -> requests.requestMatchers(publicPath).permitAll());
							securedPaths.forEach(securedPath -> requests.requestMatchers(securedPath).authenticated());
							requests.anyRequest().denyAll();
						}).addFilterBefore(new JwtTokenValidatorFilter(publicPaths, jwtSecret), BasicAuthenticationFilter.class)
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

	@Bean
	public UserDetailsService userDetailsService() {

		var user1 = User
						.builder()
						.username("aldod")
						.password("$2a$10$98z28zfaehtyTdBbQuWJ4.lPv7Loa/FcSCg6a3NpxXc0ZcfMv2SmS")
						.roles("USER")
						.build();
		var user2 = User
						.builder()
						.username("admin")
						.password("$2a$10$BvEiq8ULNr.6/cnXmOG9huxX.MtXKs5NxXEIIT.b.rDXy6a8E95RO")
						.roles("ADMIN")
						.build();

		return new InMemoryUserDetailsManager(user1, user2);
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());

		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CompromisedPasswordChecker compromisedPasswordChecker() {
		return new HaveIBeenPwnedRestApiPasswordChecker();
	}

}
