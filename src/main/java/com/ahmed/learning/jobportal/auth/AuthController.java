package com.ahmed.learning.jobportal.auth;

import com.ahmed.learning.jobportal.constants.ApplicationConstants;
import com.ahmed.learning.jobportal.dto.LoginRequestDto;
import com.ahmed.learning.jobportal.dto.LoginResponseDto;
import com.ahmed.learning.jobportal.dto.RegisterRequestDto;
import com.ahmed.learning.jobportal.dto.UserDto;
import com.ahmed.learning.jobportal.entity.Company;
import com.ahmed.learning.jobportal.entity.JobPortalUser;
import com.ahmed.learning.jobportal.entity.Role;
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
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

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
	private final CompromisedPasswordChecker compromisedPasswordChecker;

	@PostMapping(value = "/login/public")
	public ResponseEntity<LoginResponseDto> login(@RequestBody @Validated LoginRequestDto loginRequestDto) {
		try {

			var resultAuthentication =
							authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.email(),
											loginRequestDto.password()));

			String jwtToken = jwtUtil.genertateJwtToken(resultAuthentication);
			var loggedInUser = (JobPortalUser) resultAuthentication.getPrincipal();
			logger.info("User authenticated successfully: {}", loggedInUser.getEmail());

			Company company = loggedInUser.getCompany();
			UserDto userDto = new UserDto(
							loggedInUser.getId(),
							loggedInUser.getUsername(),
							loggedInUser.getEmail(),
							loggedInUser.getMobileNumber(),
							loggedInUser.getRole().getName(),
							company != null ? company.getId() : null,
							company != null ? company.getName() : null,
							loggedInUser.getCreatedAt());

			return ResponseEntity.ok(new LoginResponseDto(HttpStatus.OK.getReasonPhrase(), userDto, jwtToken));

		} catch (BadCredentialsException e) {
			logger.warn("Invalid email or password for user: {}", loginRequestDto.email());
			return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password");
		} catch (AuthenticationException e) {
			logger.warn("Authentication failed for user: {}", loginRequestDto.email());
			return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed");
		} catch (Exception e) {
			logger.error("An unexpected error occurred while authenticating user: {}", loginRequestDto.email(), e);
			return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An Unexpected error occurred");
		}
	}

	@PostMapping(value = "/register/public")
	public ResponseEntity<HashMap<String, String>> register(@RequestBody @Validated RegisterRequestDto registerRequestDto) {
		JobPortalUser user = new JobPortalUser();
		BeanUtils.copyProperties(registerRequestDto, user);
		user.setPasswordHash(hashingUtl.hashPassword(registerRequestDto.password()));

		Role role = roleRepository
						.findByName(ApplicationConstants.ROLE_JOB_SEEKER)
						.orElseThrow(() -> new IllegalArgumentException("Role not found " + ApplicationConstants.ROLE_JOB_SEEKER));

		user.setRole(role);
		jobPortalUserRepository.save(user);

		HashMap<String, String> res = new HashMap<>();
		res.put("message", "user registered successfully");
		return ResponseEntity
						.status(HttpStatus.CREATED)
						.body(res);
	}

	private ResponseEntity<LoginResponseDto> buildErrorResponse(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(new LoginResponseDto(message, null, null));
	}
}

