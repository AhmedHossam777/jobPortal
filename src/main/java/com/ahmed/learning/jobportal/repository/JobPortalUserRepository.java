package com.ahmed.learning.jobportal.repository;

import com.ahmed.learning.jobportal.entity.JobPortalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobPortalUserRepository extends JpaRepository<JobPortalUser, Long> {
	Optional<JobPortalUser> findByEmailOrMobileNumber(String email, String mobileNumber);

}