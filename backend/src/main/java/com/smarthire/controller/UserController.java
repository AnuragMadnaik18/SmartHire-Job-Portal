package com.smarthire.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smarthire.dto.LoginRequestDto;
import com.smarthire.dto.RegisterRequestDto;
import com.smarthire.entity.User;
import com.smarthire.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {	
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto dto){
		try {
			User user = userService.registerUser(dto);
			return ResponseEntity.ok(user); 
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto dto){
		try {
			User user = userService.loginUser(dto);
			return ResponseEntity.ok(user);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getUser(@PathVariable Long id){
		try {
			User user = userService.getUserById(id);
			return ResponseEntity.ok(user);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
