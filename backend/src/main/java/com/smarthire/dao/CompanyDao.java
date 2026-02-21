package com.smarthire.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smarthire.entity.Company;
public interface CompanyDao extends JpaRepository<Company,Long> {
	List<Company> findByIsDeletedFalse();
	List<Company> findByRecruiterId(Long recruiterId);
	Optional<Company> findByIdAndRecruiterId(Long id, Long recruiterId);
}
