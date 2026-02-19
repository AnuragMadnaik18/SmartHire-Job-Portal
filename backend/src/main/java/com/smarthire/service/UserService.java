package com.smarthire.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.smarthire.dto.RegisterRequestDto;
import com.smarthire.entity.User;

public interface UserService extends UserDetailsService{
	User registerUser(RegisterRequestDto dto);
	User getUserById(Long id);
}
