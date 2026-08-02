package com.ahmed.learning.jobportal.repository;

import com.ahmed.learning.jobportal.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}