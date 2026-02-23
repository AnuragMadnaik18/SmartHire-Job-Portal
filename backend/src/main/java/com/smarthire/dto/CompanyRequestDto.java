package com.smarthire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequestDto {
	private String companyName;
	private String description;
	private String website;
	private String location;
	private Long recruiterId;
	private boolean deleted;
}
