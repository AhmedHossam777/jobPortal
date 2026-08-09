package com.ahmed.learning.jobportal.security;

import com.ahmed.learning.jobportal.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * Handles a denial once the caller is already authenticated - an authenticated request
 * with a bad CSRF token, or a role that does not cover the endpoint.
 * Anonymous denials never reach here; ExceptionTranslationFilter routes those to
 * {@link JobPortalAuthenticationEntryPoint} instead.
 */
@RequiredArgsConstructor
public class JobPortalAccessDeniedHandler implements AccessDeniedHandler {
	private final ObjectMapper objectMapper;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
	                   AccessDeniedException accessDeniedException) throws IOException {
		ErrorResponseDto errorResponseDto = new ErrorResponseDto(
						"uri=" + request.getRequestURI(),
						HttpStatus.FORBIDDEN,
						accessDeniedException.getMessage(),
						LocalDateTime.now());

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		objectMapper.writeValue(response.getWriter(), errorResponseDto);
	}
}
