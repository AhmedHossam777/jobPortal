package com.ahmed.learning.jobportal.repository;

import com.ahmed.learning.jobportal.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}