package com.smarthire.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.smarthire.dto.ChangePasswordRequestDto;
import com.smarthire.dto.RegisterRequestDto;
import com.smarthire.dto.UpdateProfileRequestDto;
import com.smarthire.entity.User;

public interface UserService extends UserDetailsService{
	User registerUser(RegisterRequestDto dto);
	User getUserById(Long id);
	void updateProfile(Long userId, UpdateProfileRequestDto dto);
	void changePassword(Long userId,ChangePasswordRequestDto dto);
}
