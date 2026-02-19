package com.smarthire.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smarthire.custom_exceptions.EmailAlreadyExistsException;
import com.smarthire.custom_exceptions.UserNotFoundException;
import com.smarthire.dao.UserDao;
import com.smarthire.dto.RegisterRequestDto;
import com.smarthire.entity.User;
import com.smarthire.enums.AccountStatus;
import com.smarthire.enums.Role;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(RegisterRequestDto dto) {

        if (userDao.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered!");
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(Role.valueOf(dto.getRole().toUpperCase()));
        user.setAccountStatus(AccountStatus.ACTIVE);

        return userDao.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // 🔐 Required for Spring Security
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userDao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
