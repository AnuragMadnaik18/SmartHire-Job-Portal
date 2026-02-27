package com.smarthire.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.smarthire.custom_exceptions.ApplicationException;
import com.smarthire.dao.ApplicationDao;
import com.smarthire.dao.JobDao;
import com.smarthire.dao.UserDao;
import com.smarthire.dto.ApplicationResponseDto;
import com.smarthire.entity.Application;
import com.smarthire.entity.Job;
import com.smarthire.entity.User;
import com.smarthire.enums.ApplicationStatus;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService{

	@Autowired
	private ApplicationDao applicationDao;
	
	@Autowired
	private JobDao jobDao;
	
	@Autowired
	private UserDao userDao;
	
	private final String uploadDir = "uploads/resumes/";
	
	@Override
	public ApplicationResponseDto applyToJob(Long jobId, 
			MultipartFile resume, String coverLetter, 
			Long applicantId) {
		try {
			// Validate file
	        if (resume == null || resume.isEmpty()) {
	            throw new ApplicationException("Resume file is empty");
	        }

	        if (!"application/pdf".equals(resume.getContentType())) {
	            throw new ApplicationException("Only PDF files are allowed");
	        }
	        
			Job job = jobDao.findById(jobId) 
					.orElseThrow(()-> new ApplicationException("Job not found"));
			
			User user = userDao.findById(applicantId)
					.orElseThrow(()-> new ApplicationException("User not found"));
			
			// Prevent duplicate application
	        if (applicationDao.existsByJobIdAndApplicantId(jobId, applicantId)) {
	            throw new ApplicationException("You have already applied for this job");
	        }

	        // Create directory safely
	        Path uploadPath = Paths.get(System.getProperty("user.dir"), uploadDir);

	        if (!Files.exists(uploadPath)) {
	            Files.createDirectories(uploadPath);
	        }
			
	        
	        // Generate unique filename
			String fileName = UUID.randomUUID()+"_"+resume.getOriginalFilename();
			Path filePath = uploadPath.resolve(fileName);

	        // Save file
	        Files.copy(resume.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			
			Application application = new Application();
            application.setJob(job);
            application.setApplicant(user);
            application.setResumePath(filePath.toString());
            application.setCoverLetter(coverLetter);
            application.setAppliedDate(LocalDateTime.now());

            applicationDao.save(application);

            return mapToDto(application);
		} catch (IOException e) {
			throw new ApplicationException("File upload failed"+e.getMessage());
		}
	}

	@Override
	public List<ApplicationResponseDto> getApplicationsByJob(Long jobId) {
		return applicationDao.findByjobId(jobId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
	}

	@Override
	public List<ApplicationResponseDto> getApplicationsByApplicant(Long applicantId) {
		return applicationDao.findByApplicantId(applicantId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
	}

	@Override
	public ApplicationResponseDto updateStatus(Long applicantId, String status) {
		Application app = applicationDao.findById(applicantId)
                .orElseThrow(() -> new ApplicationException("Application not found"));

        app.setApplicationStatus(ApplicationStatus.valueOf(status));
        applicationDao.save(app);

        return mapToDto(app);
	}
	
	private ApplicationResponseDto mapToDto(Application app) {
        return new ApplicationResponseDto(
                app.getId(),
                app.getJob().getId(),
                app.getApplicant().getId(),
                app.getResumePath(),
                app.getCoverLetter(),
                app.getApplicationStatus().name(),
                app.getAppliedDate()
        );
    }

}
