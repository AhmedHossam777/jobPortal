package com.ahmed.learning.jobportal.job.controller;

import com.ahmed.learning.jobportal.dto.JobDto;
import com.ahmed.learning.jobportal.job.service.IJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {
	private final IJobService iJobService;

	@GetMapping("/{companyId}")
	ResponseEntity<List<JobDto>> getJobsOfCompany(@PathVariable Long companyId) {
		List<JobDto> jobDtos = iJobService.getCompanyJobs(companyId);
		return ResponseEntity.ok(jobDtos);
	}
}
