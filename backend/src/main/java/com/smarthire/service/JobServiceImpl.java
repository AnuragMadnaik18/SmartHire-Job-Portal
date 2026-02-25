package com.smarthire.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smarthire.custom_exceptions.JobException;
import com.smarthire.dao.CompanyDao;
import com.smarthire.dao.JobDao;
import com.smarthire.dao.UserDao;
import com.smarthire.dto.JobRequestDto;
import com.smarthire.dto.JobResponseDto;
import com.smarthire.entity.Company;
import com.smarthire.entity.Job;
import com.smarthire.entity.User;
import com.smarthire.enums.ExperienceLevel;
import com.smarthire.enums.JobStatus;
import com.smarthire.enums.JobType;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class JobServiceImpl implements JobService{

	@Autowired
	private JobDao jobDao;
	
	@Autowired
	private CompanyDao compnanyDao;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public JobResponseDto createJob(JobRequestDto dto, String email) {
		try {
			User recruiter = userDao.findByEmail(email)
					.orElseThrow(()-> new JobException("Recruiter not found"));
			Company company = compnanyDao.findById(dto.getCompanyId())
					.orElseThrow(()-> new JobException("Company not found"));
			if(!company.getRecruiter().getEmail().equals(email))
				throw new JobException("You cannot post job for another recruiter's company");
			
			Job job = new Job();
			job.setTitle(dto.getTitle());
			job.setDescription(dto.getDescription());
			job.setSalary(dto.getSalary());
			job.setLocation(dto.getLocation());
			job.setExperience(ExperienceLevel.valueOf(dto.getExperience()));
			job.setJobType(JobType.valueOf(dto.getJobType()));
			job.setStatus(JobStatus.OPEN);
			job.setCompany(company);
			job.setRecruiter(recruiter);
			
			Job savedJob = jobDao.save(job);
			
			return mapToDto(savedJob);
		} catch (Exception e) {
			throw new JobException("Error while creating job: "+e.getMessage());
		}
	}

	@Override
	public List<JobResponseDto> getJobsByRecruiterAndCompany(Long recruiterId, Long companyId) {

	    List<Job> jobs = jobDao
	            .findByRecruiterIdAndCompanyId(recruiterId, companyId);

	    return jobs.stream()
	            .map(this::mapToDto)
	            .toList();
	}

	@Override
	public List<JobResponseDto> getAllJobs() {
		return jobDao.findAll()
				.stream()
				.map(this::mapToDto)
				.toList();
	}

	@Override
	public JobResponseDto toggleJobStatus(Long jobId, String email) {
		
		User recruiter = userDao.findByEmail(email)
	            .orElseThrow(() -> new JobException("Recruiter not found"));
		
		Job job = jobDao.findById(jobId)
                .orElseThrow(() -> new JobException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new JobException("You cannot modify this job");
        }

        job.setStatus(job.getStatus() == JobStatus.OPEN ? JobStatus.CLOSED : JobStatus.OPEN);
        return mapToDto(jobDao.save(job));
	}
	
	private JobResponseDto mapToDto(Job job) {
        JobResponseDto dto = new JobResponseDto();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setSalary(job.getSalary());
        dto.setLocation(job.getLocation());
        dto.setExperience(job.getExperience().name());
        dto.setJobType(job.getJobType().name());
        dto.setStatus(job.getStatus().name());
        dto.setCompanyId(job.getCompany().getId());
        dto.setRecruiterId(job.getRecruiter().getId());
        return dto;
    }

	@Override
	public long getTotaJobsByRecruiter(Long recruiterId) {
		return jobDao.countByCompanyRecruiterId(recruiterId);
	}

	

}
