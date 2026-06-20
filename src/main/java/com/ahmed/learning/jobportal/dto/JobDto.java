package com.ahmed.learning.jobportal.dto;

import com.ahmed.learning.jobportal.entity.Job;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link Job}
 */
public record JobDto(Long id, @NotNull @Size(max = 255) String title,
                     Long companyId, String companyName, String companyLogo,
                     @NotNull @Size(max = 255) String location,
                     @NotNull @Size(max = 50) String workType,
                     @NotNull @Size(max = 50) String jobType,
                     @NotNull @Size(max = 100) String category,
                     @NotNull @Size(max = 50) String experienceLevel,
                     @NotNull BigDecimal salaryMin,
                     @NotNull BigDecimal salaryMax,
                     @NotNull @Size(max = 10) String salaryCurrency,
                     @NotNull @Size(max = 20) String salaryPeriod,
                     @NotNull String description, String requirements,
                     String benefits, @NotNull Instant postedDate,
                     Instant applicationDeadline, Integer applicationsCount,
                     Boolean featured, Boolean urgent, Boolean remote,
                     @NotNull @Size(max = 20) String status) implements Serializable {
}