package com.ahmed.learning.jobportal.auth;

import com.ahmed.learning.jobportal.dto.LoginRequestDto;
import com.ahmed.learning.jobportal.dto.LoginResponseDto;
import com.ahmed.learning.jobportal.dto.RegisterRequestDto;
import com.ahmed.learning.jobportal.dto.UserDto;
import com.ahmed.learning.jobportal.entity.JobPortalUser;
import com.ahmed.learning.jobportal.repository.JobPortalUserRepository;
import com.ahmed.learning.jobportal.repository.RoleRepository;
import com.ahmed.learning.jobportal.security.util.HashingUtl;
import com.ahmed.learning.jobportal.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
	private final Logger logger = LoggerFactory.getLogger(AuthController.class);
	private final JwtUtil jwtUtil;
	private final HashingUtl hashingUtl;

	private final JobPortalUserRepository jobPortalUserRepository;
	private final RoleRepository roleRepository;

	@PostMapping(value = "/login/public")
	public ResponseEntity<LoginResponseDto> login(@RequestBody @Validated LoginRequestDto loginRequestDto) {
		try {

			var resultAuthentication =
							authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.username(),
											loginRequestDto.password()));

			String jwtToken = jwtUtil.genertateJwtToken(resultAuthentication);
			logger.info("User authenticated successfully: {}", resultAuthentication.getPrincipal());

			return ResponseEntity.ok(new LoginResponseDto(HttpStatus.OK.getReasonPhrase(), new UserDto(1L, "dod", "dod" +
							"@email" +
							".com",
							"0213421421", "user", 12L, "KPGM", Instant.now()), jwtToken));
		} catch (BadCredentialsException e) {
			logger.warn("Invalid email or password for user: {}", loginRequestDto.username());
			return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password");
		} catch (AuthenticationException e) {
			logger.warn("Authentication failed for user: {}", loginRequestDto.username());
			return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed");
		} catch (Exception e) {
			logger.error("An unexpected error occurred while authenticating user: {}", loginRequestDto.username(), e);
			return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An Unexpected error occurred");
		}
	}

	@PostMapping(value = "/register/public")
	public ResponseEntity<String> register(@RequestBody @Validated RegisterRequestDto registerRequestDto) {
		JobPortalUser user = new JobPortalUser();
		BeanUtils.copyProperties(registerRequestDto, user);
		user.setPasswordHash(hashingUtl.hashPassword(registerRequestDto.password()));

		roleRepository.findById(1L).ifPresent(user::setRole);
		
		jobPortalUserRepository.save(user);

		return ResponseEntity
						.status(HttpStatus.CREATED)
						.body("User Registered Successfully!");
	}

	private ResponseEntity<LoginResponseDto> buildErrorResponse(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(new LoginResponseDto(message, null, null));
	}
}

