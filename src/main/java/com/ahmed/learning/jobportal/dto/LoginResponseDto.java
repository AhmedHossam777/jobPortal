package com.ahmed.learning.jobportal.dto;

public record LoginResponseDto(
				String message, UserDto user, String jwtToken
) {
}
