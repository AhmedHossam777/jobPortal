package com.ahmed.learning.jobportal.job.service;

import com.ahmed.learning.jobportal.dto.JobDto;

import java.util.List;

public interface IJobService {
	List<JobDto> getCompanyJobs(Long companyId);
}
