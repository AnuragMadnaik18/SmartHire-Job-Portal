package com.smarthire.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.smarthire.dto.ChangePasswordRequestDto;
import com.smarthire.dto.LoginRequestDto;
import com.smarthire.dto.RegisterRequestDto;
import com.smarthire.dto.UpdateProfileRequestDto;
import com.smarthire.dto.UserResponseDto;
import com.smarthire.entity.User;
import com.smarthire.service.AuthService;
import com.smarthire.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto dto) {
        try {
            User user = userService.registerUser(dto);
            
            UserResponseDto response = new UserResponseDto(
            		user.getId(),
            		user.getFullName(),
            		user.getEmail(),
            		user.getPhoneNumber(),
            		user.getRole().name());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto dto) {
        try {
            return ResponseEntity.ok(authService.login(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/update-profile/{id}")
    public ResponseEntity<String> updateProfile(@PathVariable Long id,@RequestBody UpdateProfileRequestDto dto){
    	userService.updateProfile(id, dto);
    	return ResponseEntity.ok("Profile updated successfully");
    }
    
    @PutMapping("/change-password/{id}")
    public ResponseEntity<String> changePassword(@PathVariable Long id,@RequestBody ChangePasswordRequestDto dto){
    	userService.changePassword(id, dto);
    	return ResponseEntity.ok("Password changed successfully");
    }
}
