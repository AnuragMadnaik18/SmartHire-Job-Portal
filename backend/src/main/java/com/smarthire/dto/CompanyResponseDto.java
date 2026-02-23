package com.smarthire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponseDto {
	private Long id;
    private String companyName;
    private String description;
    private String website;
    private String location;
    private String recruiterName;
    private Long recruiterId;
    private boolean deleted;
}
