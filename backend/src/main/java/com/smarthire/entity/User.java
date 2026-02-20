package com.smarthire.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smarthire.enums.AccountStatus;
import com.smarthire.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User extends BaseEntity implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="full_name",nullable = false)
	private String fullName ;
	
	@Column(unique = true,nullable = false)
	private String email ;
	
	@JsonIgnore
	@Column(nullable = false)
	private String password;
	
	@Column(name="phone_number")
	private String phoneNumber;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role; // ADMIN / RECRUITER / JOBSEEKER
	
	@Enumerated(EnumType.STRING)
	@Column(name="account_status",nullable = false)
	private AccountStatus accountStatus; // ACTIVE / BLOCKED / DEACTIVATED
	
	// 🔐 REQUIRED FOR SPRING SECURITY

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    public String getUsername() {
        return email;
    }

    public boolean isAccountNonExpired() {
        return accountStatus == AccountStatus.ACTIVE;
    }

    public boolean isAccountNonLocked() {
        return accountStatus == AccountStatus.ACTIVE;
    }

    public boolean isCredentialsNonExpired() {
        return accountStatus == AccountStatus.ACTIVE;
    }

    public boolean isEnabled() {
        return accountStatus == AccountStatus.ACTIVE;
    }
}

