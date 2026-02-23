package com.smarthire.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smarthire.entity.Company;
public interface CompanyDao extends JpaRepository<Company,Long> {
	List<Company> findByIsDeletedFalse();
//	List<Company> findByRecruiterIdAndIsDeletedFalse(Long recruiterId);
	List<Company> findByRecruiterId(Long recruiterId);
	Optional<Company> findByIdAndRecruiterIdAndIsDeletedFalse(Long id, Long recruiterId);
	Optional<Company> findByIdAndIsDeletedFalse(Long id);
	Optional<Company> findByIdAndRecruiterIdAndIsDeletedTrue(Long id, Long recruiterId);
}
