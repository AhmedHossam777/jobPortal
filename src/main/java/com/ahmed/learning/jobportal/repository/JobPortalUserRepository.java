package com.ahmed.learning.jobportal.repository;

import com.ahmed.learning.jobportal.entity.JobPortalUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPortalUserRepository extends JpaRepository<JobPortalUser, Long> {
}