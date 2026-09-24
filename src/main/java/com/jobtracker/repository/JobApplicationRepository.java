package com.jobtracker.repository;

import com.jobtracker.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByUserId(Long userId);

    List<JobApplication> findByUserIdAndStatusIgnoreCase(
            Long userId,
            String status);

    List<JobApplication> findByUserIdAndCompanyNameContainingIgnoreCase(
            Long userId,
            String companyName);

    List<JobApplication> findByUserIdAndJobRoleContainingIgnoreCase(
            Long userId,
            String jobRole);

    Page<JobApplication> findByUserId(
            Long userId,
            Pageable pageable);

    long countByUserId(Long userId);

    long countByUserIdAndStatusIgnoreCase(
            Long userId,
            String status);
}