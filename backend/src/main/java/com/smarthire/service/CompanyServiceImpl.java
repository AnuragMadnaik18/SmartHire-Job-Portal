package com.smarthire.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
		
		Authentication authentication = SecurityContextHolder
				.getContext()
				.getAuthentication();
		String email = authentication.getName();
		User recruiter = userDao.findByEmail(email)
				.orElseThrow(()-> new RuntimeException("User not found"));
		return companyDao.findByRecruiterId(recruiter.getId())
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
		
				// Get Logged-in recruiter email
				Authentication authentication = SecurityContextHolder
						.getContext()
						.getAuthentication();
				String email = authentication.getName();
				
				// Fetch Logged-in recruiter
				User recruiter = userDao.findByEmail(email)
						.orElseThrow(() -> new RuntimeException("User not found"));
				
				Company company = companyDao
					    .findByIdAndRecruiterId(id, recruiter.getId())
					    .orElseThrow(() -> new RuntimeException("Not authorized"));
				
				// Check ownership
				if(!company.getRecruiter().getId().equals(recruiter.getId())) {
					throw new RuntimeException("You are not authorized to delete this company");
				}
				
				// Soft delete
				company.setDeleted(true);
				companyDao.save(company);
	}
	
	private CompanyResponseDto mapToResponse(Company company) {
		CompanyResponseDto dto = new CompanyResponseDto();
		dto.setId(company.getId());
		dto.setCompanyName(company.getCompanyName());
		dto.setDescription(company.getDescription());
		dto.setWebsite(company.getWebsite());
		dto.setLocation(company.getLocation());
		
		if (company.getRecruiter() != null) {
	        dto.setRecruiterName(company.getRecruiter().getFullName());
	        dto.setRecruiterId(company.getRecruiter().getId());  // 🔥 THIS LINE IS IMPORTANT
	    }
		return dto;
	} 

}
