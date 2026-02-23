package com.smarthire.service;

import java.util.List;

import com.smarthire.dto.CompanyRequestDto;
import com.smarthire.dto.CompanyResponseDto;

public interface CompanyService {
	CompanyResponseDto createCompany(CompanyRequestDto dto);
	List<CompanyResponseDto> getAllCompanies();
	CompanyResponseDto getCompanyById(Long id);
	void softDeleteCompany(Long id);
	void restoreCompany(Long companyId, Long recruiterId);
}
