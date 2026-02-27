package com.smarthire.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smarthire.entity.Application;

public interface ApplicationDao extends JpaRepository<Application, Long>{
	List<Application> findByjobId(Long jobId);
	List<Application> findByApplicantId(Long applicantId);
	boolean existsByJobIdAndApplicantId(Long jobId, Long applicantId);
}
