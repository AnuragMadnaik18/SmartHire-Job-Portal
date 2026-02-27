package com.smarthire.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.smarthire.custom_exceptions.UserNotFoundException;
import com.smarthire.dao.UserDao;
import com.smarthire.dto.ApplicationResponseDto;
import com.smarthire.entity.User;
import com.smarthire.service.ApplicationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/applications")
@SecurityRequirement(name="bearerAuth")
public class ApplicationController {
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private UserDao userDao;
	
    // Apply To Job 
	@PostMapping(value = "/apply", consumes =MediaType.MULTIPART_FORM_DATA_VALUE )
	public ResponseEntity<ApplicationResponseDto> applyToJob(
	        @RequestParam Long jobId,
	        @RequestPart("resume") MultipartFile resume,
	        @RequestPart("coverLetter") String coverLetter,
	        Authentication authentication) {

	    try {
	        // 🔹 Get logged-in user email from JWT
	        String email = authentication.getName();

	        User user = userDao.findByEmail(email)
	                .orElseThrow(() ->
	                        new UserNotFoundException("User not found with email: " + email)
	                );

	        ApplicationResponseDto response =
	                applicationService.applyToJob(
	                        jobId,
	                        resume,
	                        coverLetter,
	                        user.getId()
	                );

	        return ResponseEntity.status(HttpStatus.CREATED).body(response);

	    } catch (UserNotFoundException ex) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(null);

	    } catch (Exception ex) {
	    	ex.printStackTrace(); 
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(null);
	    }
	}

    // Get Applications By Job 
    
    @GetMapping("/job/{jobId}")
    public ResponseEntity<?> getApplicationsByJob(@PathVariable Long jobId) {

        try {
            List<ApplicationResponseDto> list =
                    applicationService.getApplicationsByJob(jobId);

            return ResponseEntity.ok(list);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

   
    // Get My Applications (Applicant)
    
    @GetMapping("/my")
    public ResponseEntity<?> getMyApplications(Authentication authentication) {

        try {
            String email = authentication.getName();

            User user = userDao.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            List<ApplicationResponseDto> list =
                    applicationService.getApplicationsByApplicant(user.getId());

            return ResponseEntity.ok(list);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    
    // Update Application Status 
   
    @PutMapping("/status/{applicationId}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long applicationId,
            @RequestParam String status) {

        try {
            ApplicationResponseDto response =
                    applicationService.updateStatus(applicationId, status);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
		    
}
