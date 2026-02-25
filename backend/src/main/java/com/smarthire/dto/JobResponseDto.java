package com.smarthire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobResponseDto {
	private Long id;
	private String title;
	private String description;
	private Double salary;
	private String location;
	private String experience;
	private String jobType;
	private String status;
	private Long companyId;
	private Long recruiterId;
}
