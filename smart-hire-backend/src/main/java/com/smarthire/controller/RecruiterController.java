package com.smarthire.controller;

import com.smarthire.dto.CandidateRankingDto;
import com.smarthire.dto.CreateJobRequest;
import com.smarthire.dto.JobDto;
import com.smarthire.dto.ApplicationDto;
import com.smarthire.entity.enums.ApplicationStatus;
import com.smarthire.security.UserPrincipal;
import com.smarthire.service.ApplicationService;
import com.smarthire.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recruiter")
@PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
public class RecruiterController {

    private final JobService jobService;
    private final ApplicationService applicationService;

    public RecruiterController(JobService jobService, ApplicationService applicationService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
    }

    @PostMapping("/jobs")
    public ResponseEntity<JobDto> createJob(@AuthenticationPrincipal UserPrincipal principal,
                                            @Valid @RequestBody CreateJobRequest request) {
        return ResponseEntity.ok(jobService.createJob(principal, request));
    }

    @PutMapping("/jobs/{id}")
    public ResponseEntity<JobDto> updateJob(@AuthenticationPrincipal UserPrincipal principal,
                                            @PathVariable Long id,
                                            @Valid @RequestBody CreateJobRequest request) {
        return ResponseEntity.ok(jobService.updateJob(principal, id, request));
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Map<String, String>> deleteJob(@AuthenticationPrincipal UserPrincipal principal,
                                                         @PathVariable Long id) {
        jobService.deleteJob(principal, id);
        return ResponseEntity.ok(Map.of("message", "Job posting deactivated successfully"));
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobDto>> getRecruiterJobs(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(jobService.getRecruiterJobs(principal));
    }

    @GetMapping("/jobs/{jobId}/candidates")
    public ResponseEntity<List<CandidateRankingDto>> getCandidatesForJob(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getCandidatesForJob(principal, jobId));
    }

    @PatchMapping("/applications/{id}/status")
    public ResponseEntity<ApplicationDto> updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status) {
        return ResponseEntity.ok(applicationService.updateStatus(id, status));
    }
}
