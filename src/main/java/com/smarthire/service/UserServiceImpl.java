package com.smarthire.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smarthire.custom_exceptions.EmailAlreadyExistsException;
import com.smarthire.custom_exceptions.UserNotFoundException;
import com.smarthire.dao.UserDao;
import com.smarthire.dto.LoginRequestDto;
import com.smarthire.dto.RegisterRequestDto;
import com.smarthire.entity.User;
import com.smarthire.enums.AccountStatus;
import com.smarthire.enums.Role;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public User registerUser(RegisterRequestDto dto) {
		if(userDao.existsByEmail(dto.getEmail())) {
			throw new EmailAlreadyExistsException("Email already registered!");
		}
		User user = new User();
		user.setFullName(dto.getFullName());
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		user.setPhoneNumber(dto.getPhoneNumber());
		user.setRole(Role.valueOf(dto.getRole().toUpperCase()));
		user.setAccountStatus(AccountStatus.ACTIVE);
		
		return userDao.save(user);
	}

	@Override
	public User loginUser(LoginRequestDto dto) {
		User user = userDao.findByEmail(dto.getEmail())
				.orElseThrow(()->new UserNotFoundException("User not found"));
		if(!user.getPassword().equals(dto.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}
		
		return user;
	}

	@Override
	public User getUserById(Long id) {
		return userDao.findById(id)
				.orElseThrow(()->new UserNotFoundException("User not found"));
	}
	
}
