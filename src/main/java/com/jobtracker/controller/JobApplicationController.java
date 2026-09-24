package com.jobtracker.controller;

import com.jobtracker.dto.DashboardResponse;
import com.jobtracker.dto.JobApplicationResponse;
import com.jobtracker.entity.JobApplication;
import com.jobtracker.service.JobApplicationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(
            JobApplicationService jobApplicationService) {

        this.jobApplicationService = jobApplicationService;
    }

    // Add new job application
    @PostMapping
    public JobApplicationResponse addJobApplication(
            @Valid @RequestBody JobApplication jobApplication,
            HttpServletRequest request) {

        Long userId =
                (Long) request.getAttribute("userId");

        return jobApplicationService.saveJobApplication(
                jobApplication,
                userId);
    }

    // Get paginated applications
    @GetMapping
    public Page<JobApplicationResponse> getAllJobApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            HttpServletRequest request) {

        Long userId =
                (Long) request.getAttribute("userId");

        Pageable pageable =
                PageRequest.of(page, size);

        return jobApplicationService
                .getPaginatedApplications(
                        userId,
                        pageable);
    }

    // Get dashboard statistics
    @GetMapping("/dashboard")
    public DashboardResponse getDashboard(
            HttpServletRequest request) {

        Long userId =
                (Long) request.getAttribute("userId");

        return jobApplicationService
                .getDashboard(userId);
    }

    // Get all applications of logged-in user
    @GetMapping("/my-applications")
    public List<JobApplicationResponse> getMyApplications(
            HttpServletRequest request) {

        Long userId =
                (Long) request.getAttribute("userId");

        return jobApplicationService
                .getApplicationsByUserId(userId);
    }

    // Get applications by status
    @GetMapping("/status/{status}")
    public List<JobApplicationResponse> getApplicationsByStatus(
            @PathVariable String status,
            HttpServletRequest request) {

        Long userId =
                (Long) request.getAttribute("userId");

        return jobApplicationService
                .getApplicationsByStatus(
                        userId,
                        status);
    }

    // Search applications
    @GetMapping("/search")
    public List<JobApplicationResponse> searchApplications(
            @RequestParam String keyword,
            HttpServletRequest request) {

        Long userId =
                (Long) request.getAttribute("userId");

        return jobApplicationService
                .searchApplications(
                        userId,
                        keyword);
    }

    // Get application by ID
    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> getJobApplicationById(
            @PathVariable Long id,
            HttpServletRequest request) {

        Long userId =
                (Long) request.getAttribute("userId");

        return jobApplicationService
                .getJobApplicationById(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update application
    @PutMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> updateJobApplication(
            @PathVariable Long id,
            @Valid @RequestBody JobApplication jobApplication,
            HttpServletRequest request) {

        Long userId =
                (Long) request.getAttribute("userId");

        return jobApplicationService
                .updateJobApplication(
                        id,
                        jobApplication,
                        userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete application
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobApplication(
            @PathVariable Long id,
            HttpServletRequest request) {

        Long userId =
                (Long) request.getAttribute("userId");

        jobApplicationService
                .deleteJobApplication(id, userId);

        return ResponseEntity.noContent().build();
    }
}