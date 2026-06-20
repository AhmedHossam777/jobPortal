package com.ahmed.learning.jobportal.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record CompanyDto(
				Long id, String name, String logo, String industry, String description,
				String size, BigDecimal rating, String locations, Integer founded,
				int employees, String website, Instant createdAt
//				,List<JobDto> jobs
) {
}
