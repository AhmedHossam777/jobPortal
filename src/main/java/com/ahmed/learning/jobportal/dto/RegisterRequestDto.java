package com.ahmed.learning.jobportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record RegisterRequestDto(
				@Size(message = "The Length of name must be between 5 and 30", min = 5, max = 30)
				@NotBlank(message = "Name is required")
				String username,

				@NotBlank(message = "Email is required")
				String email,

				@NotBlank(message = "Password is required")
				@Size(min = 6, max = 20, message = "Password length must be between 6 and 20")
				String password,

				@Size(message = "Mobile number must be excactly 10 digits", min = 10, max = 10)
				@NotBlank(message = "Mobile number is required")
				String mobileNumber

) implements Serializable {
}