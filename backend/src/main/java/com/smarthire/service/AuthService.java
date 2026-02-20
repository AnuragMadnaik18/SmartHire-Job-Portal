package com.smarthire.service;

import com.smarthire.dto.LoginRequestDto;
import com.smarthire.dto.LoginResponseDto;

public interface AuthService {
	LoginResponseDto login(LoginRequestDto request);
}
