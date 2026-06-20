package com.ahmed.learning.jobportal.repository;

import com.ahmed.learning.jobportal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
	List<Job> getJobsByCompanyIdIs(Long companyId);
}