package com.ahmed.learning.jobportal.security;

import com.ahmed.learning.jobportal.constants.ApplicationConstants;
import com.ahmed.learning.jobportal.security.filter.JwtTokenValidatorFilter;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JobPortalSecurityConfig {
	@Qualifier("publicPaths")
	private final List<String> publicPaths;

	@Qualifier("securedPaths")
	private final List<String> securedPaths;

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, Environment env, ObjectMapper objectMapper) {
		// resolved here, not inside the filter: a filter registered with addFilterBefore is
		// not a bean, so its own getEnvironment() would not see the application properties
		String jwtSecret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
						ApplicationConstants.JWT_DEFAULT_VALUE);

		AuthenticationEntryPoint authenticationEntryPoint = new JobPortalAuthenticationEntryPoint(objectMapper);
		AccessDeniedHandler accessDeniedHandler = new JobPortalAccessDeniedHandler(objectMapper);

		return http
						.csrf(csrfConfig -> csrfConfig
										.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
										.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
						.cors(corsConfig -> corsConfig.configurationSource(corsConfigurationSource()))
						.authorizeHttpRequests((requests) -> {
							// the chain also runs on the ERROR dispatch, where /error matches no rule
							// and hits denyAll - without this every 404 and unhandled exception is
							// rewritten into a misleading 401
							requests.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll();
							publicPaths.forEach(publicPath -> requests.requestMatchers(publicPath).permitAll());
							securedPaths.forEach(securedPath -> requests.requestMatchers(securedPath).authenticated());
							requests.anyRequest().denyAll();
						})
						.addFilterBefore(new JwtTokenValidatorFilter(publicPaths, jwtSecret, authenticationEntryPoint),
										BasicAuthenticationFilter.class)
						.exceptionHandling(exceptionConfig -> exceptionConfig
										.authenticationEntryPoint(authenticationEntryPoint)
										.accessDeniedHandler(accessDeniedHandler))
						.formLogin(AbstractHttpConfigurer::disable)
						// credentials are exchanged for a JWT at /api/auth/login/public, so there is no
						// browser Basic auth to fall back on - and its entry point answered every
						// rejection with a bodyless 401 that hid the real cause
						.httpBasic(AbstractHttpConfigurer::disable)
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
	public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
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
