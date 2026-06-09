package com.ahmed.learning.jobportal.dto;

public record ContactResponseDto(Long id, String name, String email,
                                 String message, String userType,
                                 String subject, String status) {
}
