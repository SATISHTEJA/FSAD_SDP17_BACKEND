package com.bst.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bst.dto.ApplicationDTO;
import com.bst.model.Application;
import com.bst.model.ApplicationStatus;
import com.bst.model.Internship;
import com.bst.model.Student;
import com.bst.repo.ApplicationRepo;
import com.bst.repo.InternshipRepo;
import com.bst.repo.StudentRepo;

@Service
public class ApplicationServiceImpl implements ApplicationService {

	@Autowired
	private StudentRepo studentRepo;

	@Autowired
	private InternshipRepo internshipRepo;

	@Autowired
	private ApplicationRepo applicationRepo;

	
    @Override
    public List<ApplicationDTO> getApplicationsByStudent(Long studentId) {

        List<Application> apps =
            applicationRepo.findByStudentIdWithInternship(studentId);

        return apps.stream().map(app -> {

            ApplicationDTO dto = new ApplicationDTO();

            dto.setId(app.getId());
            dto.setStatus(app.getStatus().name());
            dto.setRole(app.getRole());
            dto.setUniversity(app.getUniversity());
            dto.setDescription(app.getInternship().getDescription());
            dto.setAppliedDate(
                app.getAppliedDate() != null
                    ? app.getAppliedDate().toString()
                    : null
            );

            dto.setInternshipTitle(app.getInternship().getTitle());
            dto.setCompanyName(app.getInternship().getCompanyname());
            dto.setLocation(app.getInternship().getLocation());
            dto.setDuration(app.getInternship().getDuration());
            dto.setStipend(app.getInternship().getStipend());

            return dto;

        }).toList();
    }

    @Override
    public Application updateStatus(Long applicationId, String status) {
        Application app = applicationRepo.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus(ApplicationStatus.valueOf(status.toUpperCase()));
        return applicationRepo.save(app);
    }

    @Override
    public void deleteApplication(Long id) {
        applicationRepo.deleteById(id);
    }

    @Override
    public Application apply(
            String fullName,
            String email,
            String role,
            String university,
            Double gpa,
            Long userId,
            Long internshipId,
            MultipartFile file
    ) throws IOException {
    	System.out.println("APPLY CALLED");

        String filePathStr = null;

        if (file != null && !file.isEmpty()) {
            String uploadDir = "uploads/";
            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, file.getBytes());

            filePathStr = uploadDir + fileName;
        }

        Student student = studentRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Internship internship = internshipRepo.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found"));

        Application app = new Application();
        app.setFullName(fullName);
        app.setEmail(email);
        app.setRole(role);
        app.setUniversity(university);
        app.setGpa(gpa);
        app.setStudent(student);
        app.setInternship(internship);
        app.setResumePath(filePathStr);
        app.setStatus(ApplicationStatus.PENDING);
        app.setAppliedDate(LocalDate.now());

        return applicationRepo.save(app);
    }
    
    @Override
    public List<Application> getApplicationsByEmployer(Long employerId) {
        return applicationRepo.findByInternshipEmployerId(employerId);
    }
    
    @Override
    public List<Application> getApplicationsByInternship(Long internshipId) {
        return applicationRepo.findByInternshipId(internshipId);
    }

	@Override
	public List<Application> getAllApplications() {
		return applicationRepo.findAll();
	}
}