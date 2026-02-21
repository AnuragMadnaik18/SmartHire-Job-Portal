package com.smarthire.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smarthire.custom_exceptions.CompanyNotFoundException;
import com.smarthire.dao.CompanyDao;
import com.smarthire.dao.UserDao;
import com.smarthire.dto.CompanyRequestDto;
import com.smarthire.dto.CompanyResponseDto;
import com.smarthire.entity.Company;
import com.smarthire.entity.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService{
	@Autowired
	private CompanyDao companyDao ;
	
	@Autowired
	private UserDao userDao ;

	@Override
	public CompanyResponseDto createCompany(CompanyRequestDto dto) {
		try {
			User recruiter = userDao.findById(dto.getRecruiterId())
					.orElseThrow(()-> new RuntimeException("Recruiter not found"));
			Company company = new Company();
			company.setCompanyName(dto.getCompanyName());
			company.setDescription(dto.getDescription());
			company.setWebsite(dto.getWebsite());
			company.setLocation(dto.getLocation());
			company.setRecruiter(recruiter);
			
			Company saved = companyDao.save(company);
			
			return mapToResponse(saved);
		}catch(Exception e) {
			throw new RuntimeException("Error creating company: "+e.getMessage());
		}
	}

	@Override
	public List<CompanyResponseDto> getAllCompanies() {
		return companyDao.findAll()
				.stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	public CompanyResponseDto getCompanyById(Long id) {
		Company company = companyDao.findById(id)
				.orElseThrow(()-> new CompanyNotFoundException("Company not found"));
		return mapToResponse(company);
	}

	@Override
	public void softDeleteCompany(Long id) {
		Company company = companyDao.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found"));

        company.setDeleted(true); // if using soft delete
        companyDao.save(company);
	}
	
	private CompanyResponseDto mapToResponse(Company company) {
		CompanyResponseDto dto = new CompanyResponseDto();
		dto.setId(company.getId());
		dto.setCompanyName(company.getCompanyName());
		dto.setDescription(company.getDescription());
		dto.setWebsite(company.getWebsite());
		dto.setLocation(company.getLocation());
		dto.setRecruiterName(company.getRecruiter().getFullName());
		return dto;
	} 

}
