package com.smarthire.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smarthire.enums.AccountStatus;
import com.smarthire.enums.ExperienceLevel;
import com.smarthire.enums.JobStatus;
import com.smarthire.enums.JobType;
import com.smarthire.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="jobs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Job extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
	@Column(length=2000)
	private String description;
	
	private Double salary;
	
	private String location;
	
	@Enumerated(EnumType.STRING)
	private ExperienceLevel experience;
	
	@Enumerated(EnumType.STRING)
	private JobType jobType;
	
	@Enumerated(EnumType.STRING)
	private JobStatus status;
	
	@ManyToOne
	@JoinColumn(name="company_id",nullable = false)
	private Company company;
	
	@ManyToOne
	@JoinColumn(name="recruiter_id",nullable=false)
	private User recruiter;
}
