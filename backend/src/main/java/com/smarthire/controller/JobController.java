package com.smarthire.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smarthire.dao.UserDao;
import com.smarthire.dto.JobRequestDto;
import com.smarthire.dto.JobResponseDto;
import com.smarthire.entity.User;
import com.smarthire.service.JobService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/jobs")
@PreAuthorize("hasAuthority('RECRUITER')")
@SecurityRequirement(name="bearerAuth")
public class JobController {
	@Autowired
	private JobService jobService;
	
	@Autowired
	private UserDao userDao;
	
	@PostMapping("/create")
    public JobResponseDto createJob(@RequestBody JobRequestDto dto) {
		Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();
	    String email = authentication.getName();
        return jobService.createJob(dto, email);
    }

	@GetMapping("/company/{companyId}")
	public ResponseEntity<List<JobResponseDto>> getJobsByCompanyForLoggedInRecruiter(
	        @PathVariable Long companyId,
	        Authentication authentication) {

	    String email = authentication.getName();

	    User recruiter =userDao.findByEmail(email)
	            .orElseThrow();

	    return ResponseEntity.ok(
	        jobService.getJobsByRecruiterAndCompany(
	            recruiter.getId(), companyId)
	    );
	}

    @GetMapping("/all")
    public List<JobResponseDto> getAllJobs() {
        return jobService.getAllJobs();
    }

    @PutMapping("/toggleStatus/{jobId}")
    public JobResponseDto closeJob(
            @PathVariable Long jobId) {
    	Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return jobService.toggleJobStatus(jobId, email);
    }
    
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalJobs(){
    	Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
    	User user = (User) authentication.getPrincipal();

        long totalJobs = jobService.getTotaJobsByRecruiter(user.getId());

        return ResponseEntity.ok(totalJobs);
    }
}
