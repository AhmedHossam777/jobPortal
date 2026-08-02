package com.ahmed.learning.jobportal.dto;

import com.ahmed.learning.jobportal.entity.JobPortalUser;

public record RegisterResponseDto(String message, JobPortalUser user, String jwtToken) {
}
