package com.smarthire.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.smarthire.dto.ApplicationResponseDto;

public interface ApplicationService {
	ApplicationResponseDto applyToJob(Long jobId,
			MultipartFile resume,String coverLetter,
			Long applicantId);
	List<ApplicationResponseDto> getApplicationsByJob(Long jobId);
	List<ApplicationResponseDto> getApplicationsByApplicant(Long applicantId);
	ApplicationResponseDto updateStatus(Long applicantId,String status);
}
