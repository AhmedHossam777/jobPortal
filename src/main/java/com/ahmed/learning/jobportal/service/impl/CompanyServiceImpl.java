package com.ahmed.learning.jobportal.service.impl;

import com.ahmed.learning.jobportal.entity.Company;
import com.ahmed.learning.jobportal.repository.CompanyInterface;
import com.ahmed.learning.jobportal.service.ICompanyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements ICompanyService {
	private final CompanyInterface companyRepository;

	public CompanyServiceImpl(CompanyInterface companyRepository) {
		this.companyRepository = companyRepository;
	}

	@Override
	public List<Company> getAllCompanies() {
		return companyRepository.findAll();
	}
}
