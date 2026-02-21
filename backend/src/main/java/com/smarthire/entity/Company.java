package com.smarthire.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="companies")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Company extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="company_name",nullable = false)
	private String companyName;
	
	@Column(length=1000)
	private String description;
	
	private String website;
	
	private String location;
	
	// Soft delete
	@Column(name="is_deleted")
	private boolean isDeleted = false;
	
	@ManyToOne
	@JoinColumn(name="recruiter_id",nullable = false)
	private User recruiter;
}
