package com.jobtracker.service;

import com.jobtracker.dto.DashboardResponse;
import com.jobtracker.entity.JobApplication;
import com.jobtracker.entity.User;
import com.jobtracker.repository.JobApplicationRepository;
import com.jobtracker.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.jobtracker.dto.JobApplicationResponse;
import com.jobtracker.exception.ResourceNotFoundException;
import com.jobtracker.exception.UnauthorizedException;

import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final UserRepository userRepository;

    public JobApplicationService(
            JobApplicationRepository jobApplicationRepository,
            UserRepository userRepository) {

        this.jobApplicationRepository = jobApplicationRepository;
        this.userRepository = userRepository;
    }

    // Save job application
    public JobApplicationResponse saveJobApplication(
            JobApplication jobApplication,
            Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        jobApplication.setUser(user);

        JobApplication savedApplication =
                jobApplicationRepository.save(jobApplication);

        return convertToResponse(savedApplication);
    }

    // Get all job applications of logged-in user
    public List<JobApplicationResponse> getAllJobApplications(Long userId) {

        return jobApplicationRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Get paginated applications of logged-in user
    public Page<JobApplicationResponse> getPaginatedApplications(
            Long userId,
            Pageable pageable) {

        return jobApplicationRepository
                .findByUserId(userId, pageable)
                .map(this::convertToResponse);
    }

    // Get job application by ID
    public Optional<JobApplicationResponse> getJobApplicationById(
            Long id,
            Long userId) {

        return jobApplicationRepository.findById(id)
                .filter(application ->
                        application.getUser() != null &&
                                application.getUser()
                                        .getId()
                                        .equals(userId))
                .map(this::convertToResponse);
    }

    // Update job application
    public Optional<JobApplicationResponse> updateJobApplication(
            Long id,
            JobApplication updatedApplication,
            Long userId) {

        return jobApplicationRepository.findById(id)
                .map(existingApplication -> {

                    if (existingApplication.getUser() == null ||
                            !existingApplication.getUser()
                                    .getId()
                                    .equals(userId)) {

                        throw new UnauthorizedException(
                                "You are not authorized to update this application");
                    }

                    existingApplication.setCompanyName(
                            updatedApplication.getCompanyName());

                    existingApplication.setJobRole(
                            updatedApplication.getJobRole());

                    existingApplication.setApplicationDate(
                            updatedApplication.getApplicationDate());

                    existingApplication.setStatus(
                            updatedApplication.getStatus());

                    existingApplication.setJobLink(
                            updatedApplication.getJobLink());

                    existingApplication.setNotes(
                            updatedApplication.getNotes());

                    JobApplication savedApplication =
                            jobApplicationRepository.save(
                                    existingApplication);

                    return convertToResponse(savedApplication);
                });
    }

    // Get job applications by user ID
    public List<JobApplicationResponse> getApplicationsByUserId(
            Long userId) {

        return jobApplicationRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Get applications by status
    public List<JobApplicationResponse> getApplicationsByStatus(
            Long userId,
            String status) {

        return jobApplicationRepository
                .findByUserIdAndStatusIgnoreCase(userId, status)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Search applications by company name or job role
    public List<JobApplicationResponse> searchApplications(
            Long userId,
            String keyword) {

        List<JobApplication> companyResults =
                jobApplicationRepository
                        .findByUserIdAndCompanyNameContainingIgnoreCase(
                                userId,
                                keyword);

        List<JobApplication> roleResults =
                jobApplicationRepository
                        .findByUserIdAndJobRoleContainingIgnoreCase(
                                userId,
                                keyword);

        companyResults.addAll(roleResults);

        return companyResults.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Get dashboard statistics
    public DashboardResponse getDashboard(Long userId) {

        long totalApplications =
                jobApplicationRepository.countByUserId(userId);

        long applied =
                jobApplicationRepository
                        .countByUserIdAndStatusIgnoreCase(
                                userId, "Applied");

        long interview =
                jobApplicationRepository
                        .countByUserIdAndStatusIgnoreCase(
                                userId, "Interview");

        long rejected =
                jobApplicationRepository
                        .countByUserIdAndStatusIgnoreCase(
                                userId, "Rejected");

        long selected =
                jobApplicationRepository
                        .countByUserIdAndStatusIgnoreCase(
                                userId, "Selected");

        return new DashboardResponse(
                totalApplications,
                applied,
                interview,
                rejected,
                selected);
    }
    // Convert JobApplication entity to response DTO
    private JobApplicationResponse convertToResponse(
            JobApplication application) {

        return new JobApplicationResponse(
                application.getId(),
                application.getCompanyName(),
                application.getJobRole(),
                application.getApplicationDate(),
                application.getStatus(),
                application.getJobLink(),
                application.getNotes()
        );
    }

    // Delete job application
    public void deleteJobApplication(Long id, Long userId) {

        JobApplication application = jobApplicationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application not found"));
        // Check if the application belongs
        // to the logged-in user
        if (application.getUser() == null ||
                !application.getUser()
                        .getId()
                        .equals(userId)) {

            throw new UnauthorizedException(
                    "You are not authorized to delete this application");
        }

        jobApplicationRepository.delete(application);
    }
}