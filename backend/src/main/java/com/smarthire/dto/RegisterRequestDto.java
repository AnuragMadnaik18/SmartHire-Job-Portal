package com.smarthire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
	private String fullName;
	private String email;
	private String password;
	private String phoneNumber;
	private String role;
}
