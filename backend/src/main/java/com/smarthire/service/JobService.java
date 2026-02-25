package com.smarthire.service;

import java.util.List;

import com.smarthire.dto.JobRequestDto;
import com.smarthire.dto.JobResponseDto;

public interface JobService {
	JobResponseDto createJob(JobRequestDto dto,String email);
	List<JobResponseDto> getJobsByRecruiterAndCompany(Long recruiterId, Long companyId);
	List<JobResponseDto> getAllJobs();
	JobResponseDto toggleJobStatus(Long jobId,String email);
	long getTotaJobsByRecruiter(Long recruiterId);
}
