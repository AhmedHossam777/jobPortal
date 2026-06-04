package com.ahmed.learning.jobportal.service.impl;

import com.ahmed.learning.jobportal.dto.CompanyDto;
import com.ahmed.learning.jobportal.entity.Company;
import com.ahmed.learning.jobportal.repository.CompanyInterface;
import com.ahmed.learning.jobportal.service.ICompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements ICompanyService {
	private final CompanyInterface companyRepository;

	@Override
	public List<CompanyDto> getAllCompanies() {
		List<Company> companyList = companyRepository.findAll();
		return companyList.stream().map(this::companyToCompanyDto).toList();
	}

	private CompanyDto companyToCompanyDto(Company company) {
		return new CompanyDto(company.getId(), company.getName(),
						company.getLogo(), company.getIndustry(), company.getDescription()
						, company.getSize(), company.getRating(), company.getLocations(),
						company.getFounded(), company.getEmployees(), company.getWebsite()
						, company.getCreatedAt());
	}
}
