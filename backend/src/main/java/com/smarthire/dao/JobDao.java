package com.smarthire.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smarthire.entity.Job;

public interface JobDao extends  JpaRepository<Job,Long> {
	List<Job> findByRecruiterIdAndCompanyId(Long recruiterId, Long companyId);
	List<Job> findByCompanyId(Long companyId);
	long countByCompanyRecruiterId(Long recruiterId);
}
