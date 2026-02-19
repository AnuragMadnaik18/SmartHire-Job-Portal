package com.smarthire.dto;

public class LoginResponseDto {
	private String token;
	private String type="Bearer";
	private Long id;
	private String fullName;
	private String email;
	private String role;
	
	public LoginResponseDto(String token,Long id,String fullname,String email,String role) {
		this.token = token;
		this.id= id ;
		this.fullName = fullname;
		this.email= email;
		this.role=role;
	}
	
	public String getToken() {return token;}
	public String getType() {return type;}
	public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}	
