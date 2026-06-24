package com.ahmed.learning.jobportal.dto;

import java.time.Instant;

public record UserDto(
				Long userId,
				String name,
				String email,
				String mobileNumber,
				String role,
				Long companyId,
				String companyName,
				Instant createdAt
) {
}
