package com.smarthire.service;

import com.smarthire.dto.LoginRequestDto;
import com.smarthire.dto.RegisterRequestDto;
import com.smarthire.entity.User;

public interface UserService {
	User registerUser(RegisterRequestDto dto);
	User loginUser(LoginRequestDto dto);
	User getUserById(Long id);
}
