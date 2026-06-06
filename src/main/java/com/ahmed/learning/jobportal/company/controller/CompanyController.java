package com.ahmed.learning.jobportal.company.controller;

import com.ahmed.learning.jobportal.company.service.ICompanyService;
import com.ahmed.learning.jobportal.dto.CompanyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {
	private final ICompanyService companyService;

	@GetMapping(version = "1.0")
	public ResponseEntity<List<CompanyDto>> getCompanies() {
		List<CompanyDto> companyList = companyService.getAllCompanies();
		return ResponseEntity.ok(companyList);
	}

	@GetMapping(version = "2.0")
	public ResponseEntity<String> getCompanies2() {
		return ResponseEntity.ok("Company List2 updated");
	}
}
