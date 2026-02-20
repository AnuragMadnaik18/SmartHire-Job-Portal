package com.smarthire.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.smarthire.dto.LoginRequestDto;
import com.smarthire.dto.LoginResponseDto;
import com.smarthire.entity.User;
import com.smarthire.security.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtUtil.createToken(authentication);
        
        return new LoginResponseDto(
        		token, 
        		user.getId(), 
        		user.getFullName(),
        		user.getEmail(),
        		user.getRole().name());
        		
    }
}

