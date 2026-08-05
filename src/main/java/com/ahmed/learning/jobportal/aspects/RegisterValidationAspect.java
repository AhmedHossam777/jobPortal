package com.ahmed.learning.jobportal.aspects;

import com.ahmed.learning.jobportal.dto.RegisterRequestDto;
import com.ahmed.learning.jobportal.entity.JobPortalUser;
import com.ahmed.learning.jobportal.exception.RegistrationValidationException;
import com.ahmed.learning.jobportal.repository.JobPortalUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RegisterValidationAspect {
	private final JobPortalUserRepository jobPortalUserRepository;
	private final CompromisedPasswordChecker compromisedPasswordChecker;

	@Before("execution(* com.ahmed.learning.jobportal.auth.AuthController.register(..))")
	public void validateRegisterRequest(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		RegisterRequestDto registerRequestDto = (RegisterRequestDto) args[0];
		log.info("Validating register request...");

		HashMap<String, String> errors = new HashMap<>();

		Optional<JobPortalUser> existingUser = jobPortalUserRepository
						.findByEmailOrMobileNumber(registerRequestDto.email(), registerRequestDto.mobileNumber());

		if (existingUser.isPresent()) {
			JobPortalUser jobPortalUser = existingUser.get();
			if (jobPortalUser.getEmail().equalsIgnoreCase(registerRequestDto.email())) {
				errors.put("email", "Email is already in use");
			}
			if (jobPortalUser.getMobileNumber().equals(registerRequestDto.mobileNumber())) {
				errors.put("mobile number", "mobile number is already in use");
			}

		}

		CompromisedPasswordDecision compromisedPasswordDecision =
						compromisedPasswordChecker.check(registerRequestDto.password());
		if (compromisedPasswordDecision.isCompromised()) {
			errors.put("password", "You provided a weak compromised password");
			log.warn("registration validation failed");
		}

		if (!errors.isEmpty()) {
			log.warn("registration validation failed: {}", errors);
			throw new RegistrationValidationException(errors);
		}
		log.info("Registration validation success!");
	}

}
