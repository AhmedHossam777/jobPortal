package com.ahmed.learning.jobportal.dto;

import java.io.Serializable;

public record ContactDto(Long id, String email,
                         String message, String name, String status,
                         String subject,
                         String userType) implements Serializable {
}