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
	private Long applicantId;
	private String resumePath;
	private String coverLetter;
	private String applicationStatus;
	private LocalDateTime appliedDate;
}
