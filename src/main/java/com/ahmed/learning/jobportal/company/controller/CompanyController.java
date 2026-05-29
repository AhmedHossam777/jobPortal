package com.ahmed.learning.jobportal.company.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
public class CompanyController {
	@GetMapping(version = "1.0")
	public ResponseEntity<String> getCompanies() {
		return ResponseEntity.ok("Company List");
	}

	@GetMapping(version = "2.0")
	public ResponseEntity<String> getCompanies2() {
		return ResponseEntity.ok("Company List2");
	}
}
