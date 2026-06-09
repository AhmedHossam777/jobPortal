package com.ahmed.learning.jobportal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record ContactDto(

				@NotBlank(message = "email cannot be empty")
				@Email(message = "invalid email address")
				String email,

				@NotBlank(message = "message cannot be empty")
				@Size(min = 5, max = 500, message = "message must be between 5 and " +
								"500" +
								" characters")
				String message,

				@NotBlank(message = "name cannot be empty")
				@Size(min = 5, max = 30, message = "name must be between 5 and 30" +
								" characters")
				String name,

				@NotBlank(message = "subject cannot be empty")
				@Size(min = 5, max = 150, message = "subject must be between 5 and " +
								"150" +
								" characters")
				String subject,

				@NotBlank(message = "status cannot be empty")
				String status,

				@NotBlank(message = "user type cannot be empty")
				@Pattern(regexp = "JobSeeker|Employer|Other", message = "user type " +
								"must be one of: JobSeeker, Employer or Other ")
				String userType) implements Serializable {
}