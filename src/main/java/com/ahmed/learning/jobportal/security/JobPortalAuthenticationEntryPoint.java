package com.ahmed.learning.jobportal.security;

import com.ahmed.learning.jobportal.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * Writes a JSON 401 instead of the bodyless challenge the Basic-auth entry point sends.
 * Echoing the request URI matters: a request that misses every permitAll rule and one
 * that fails CSRF both land here, and the path is what tells the two apart.
 */
@RequiredArgsConstructor
public class JobPortalAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
	                     AuthenticationException authException) throws IOException {
		ErrorResponseDto errorResponseDto = new ErrorResponseDto(
						"uri=" + request.getRequestURI(),
						HttpStatus.UNAUTHORIZED,
						authException.getMessage(),
						LocalDateTime.now());

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		objectMapper.writeValue(response.getWriter(), errorResponseDto);
	}
}
