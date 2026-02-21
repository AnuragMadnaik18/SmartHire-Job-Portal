package com.smarthire.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smarthire.entity.Company;
public interface CompanyDao extends JpaRepository<Company,Long> {
	List<Company> findByIsDeletedFalse();
}
