package com.smarthire.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smarthire.custom_exceptions.CompanyNotFoundException;
import com.smarthire.dao.CompanyDao;
import com.smarthire.dao.UserDao;
import com.smarthire.dto.CompanyRequestDto;
import com.smarthire.dto.CompanyResponseDto;
import com.smarthire.entity.Company;
import com.smarthire.entity.User;
import com.smarthire.service.CompanyService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
@RequestMapping("/api/company")
@SecurityRequirement(name="bearerAuth")
public class CompanyController {
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CompanyDao companyDao;
	
	 @PostMapping
	 public ResponseEntity<CompanyResponseDto> createCompany(@RequestBody CompanyRequestDto dto) {
	        CompanyResponseDto response = companyService.createCompany(dto);
	        return ResponseEntity.ok(response);
	 }

	 @GetMapping
	 public ResponseEntity<List<CompanyResponseDto>> getAllCompanies() {
	        return ResponseEntity.ok(companyService.getAllCompanies());
	 }
	 
	 
	 @GetMapping("/{id}")
	 public ResponseEntity<CompanyResponseDto> getCompanyById(@PathVariable Long id) {
	        return ResponseEntity.ok(companyService.getCompanyById(id));
	 }

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
		companyService.softDeleteCompany(id);
	    return ResponseEntity.ok("Company deleted successfully");
	}
	
	@PutMapping("/{id}/restore")
    public ResponseEntity<String> restoreCompany(@PathVariable Long id,
                                                 Authentication authentication) {

        User user = (User) authentication.getPrincipal();  // your custom UserDetails
        Long recruiterId = user.getId();

        companyService.restoreCompany(id, recruiterId);

        return ResponseEntity.ok("Company restored successfully");
    }
}
