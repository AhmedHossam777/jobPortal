package com.ahmed.learning.jobportal.repository;

import com.ahmed.learning.jobportal.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyInterface extends JpaRepository<Company, Long> {
}
