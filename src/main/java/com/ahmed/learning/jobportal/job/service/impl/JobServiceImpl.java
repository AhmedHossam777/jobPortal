package com.ahmed.learning.jobportal.job.service.impl;

import com.ahmed.learning.jobportal.dto.JobDto;
import com.ahmed.learning.jobportal.entity.Job;
import com.ahmed.learning.jobportal.job.service.IJobService;
import com.ahmed.learning.jobportal.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements IJobService {
	private final JobRepository jobRepository;

	@Override
	public List<JobDto> getCompanyJobs(Long companyId) {
		List<Job> jobs = jobRepository.getJobsByCompanyIdIs(companyId);
		List<JobDto> jobDtos = jobs
						.stream()
						.map(this::transformJobToDto)
						.toList();

		return jobDtos;
	}

	private JobDto transformJobToDto(Job job) {
		return new JobDto(job.getId(), job.getTitle(), job.getCompany().getId(),
						job
										.getCompany()
										.getName(), job
						.getCompany()
						.getLogo(), job.getLocation(), job.getWorkType(), job.getJobType()
						, job.getCategory(), job.getExperienceLevel(), job.getSalaryMin(),
						job.getSalaryMax(), job.getSalaryCurrency(), job.getSalaryPeriod()
						, job.getDescription(), job.getRequirements(), job.getBenefits(),
						job.getPostedDate(), job.getApplicationDeadline(),
						job.getApplicationsCount(), job.getFeatured(), job.getUrgent(),
						job.getRemote(), job.getStatus());
	}
}
