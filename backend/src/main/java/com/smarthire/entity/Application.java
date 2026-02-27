package com.smarthire.entity;

import java.time.LocalDateTime;

import com.smarthire.enums.ApplicationStatus;

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
@Table(name="applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Application {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// Many applications -> one job
	@ManyToOne
	@JoinColumn(name="job_id",nullable = false)
	private Job job;
	
	// Many applications -> One user(applicant)
	@ManyToOne
	@JoinColumn(name="applicant_id",nullable = false)
	private User applicant; 
	
	@Column(nullable=false)
	private String resumePath;
	
	@Column(columnDefinition = "TEXT")
	private String coverLetter;
	
	@Enumerated(EnumType.STRING)
	private ApplicationStatus applicationStatus = ApplicationStatus.APPLIED;
	
	private LocalDateTime appliedDate;
}
