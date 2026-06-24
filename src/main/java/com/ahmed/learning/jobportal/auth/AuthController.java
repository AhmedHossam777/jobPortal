package com.ahmed.learning.jobportal.auth;

import com.ahmed.learning.jobportal.dto.LoginRequestDto;
import com.ahmed.learning.jobportal.dto.LoginResponseDto;
import com.ahmed.learning.jobportal.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthenticationManager authenticationManager;

	@PostMapping(value = "/login/public", version = "1.0")
	public ResponseEntity<LoginResponseDto> login(@RequestBody @Validated LoginRequestDto loginRequestDto) {
		try {

			var resultAuthentication =
							authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.username(),
											loginRequestDto.password()));

			return ResponseEntity.ok(new LoginResponseDto(HttpStatus.OK.getReasonPhrase(), new UserDto(1L, "dod", "dod" +
							"@email" +
							".com",
							"0213421421", "user", 12L, "KPGM", Instant.now()), "test jwt"));
		} catch (BadCredentialsException e) {
			return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password");
		} catch (AuthenticationException e) {
			return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed");
		} catch (Exception e) {
			return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An Unexpected error occurred");
		}

	}

	private ResponseEntity<LoginResponseDto> buildErrorResponse(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(new LoginResponseDto(message, null, null));
	}
}

