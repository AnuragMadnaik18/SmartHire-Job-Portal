package com.smarthire.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponseDto {
	private Long id;
	private Long jobId;
	private String jobTitle;
	private String companyName;
	private Long applicantId;
	private String applicantName;
	private String resumePath;
	private String coverLetter;
	private String applicationStatus;
	private LocalDateTime appliedDate;
}
