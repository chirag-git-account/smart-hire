package com.smarthire.service;

import com.smarthire.dto.CreateJobRequest;
import com.smarthire.dto.JobDto;
import com.smarthire.entity.Job;
import com.smarthire.entity.User;
import com.smarthire.entity.enums.JobType;
import com.smarthire.repository.ApplicationRepository;
import com.smarthire.repository.JobRepository;
import com.smarthire.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final UserService userService;

    public JobService(JobRepository jobRepository, ApplicationRepository applicationRepository, UserService userService) {
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.userService = userService;
    }

    @Transactional
    public JobDto createJob(UserPrincipal principal, CreateJobRequest request) {
        User recruiter = userService.getCurrentUser(principal);

        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .jobType(request.getJobType() != null ? request.getJobType() : JobType.FULL_TIME)
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .experienceMinYears(request.getExperienceMinYears() != null ? request.getExperienceMinYears() : 0)
                .educationRequirement(request.getEducationRequirement())
                .responsibilities(request.getResponsibilities())
                .benefits(request.getBenefits())
                .deadline(request.getDeadline())
                .requiredSkills(request.getRequiredSkillNames() != null ? request.getRequiredSkillNames() : new java.util.ArrayList<>())
                .companyName(recruiter.getCompanyName() != null ? recruiter.getCompanyName() : "SmartHire Partner")
                .recruiter(recruiter)
                .isActive(true)
                .build();

        Job saved = jobRepository.save(job);
        return mapToDto(saved);
    }

    @Transactional
    public JobDto updateJob(UserPrincipal principal, Long jobId, CreateJobRequest request) {
        User recruiter = userService.getCurrentUser(principal);
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + jobId));

        if (!job.getRecruiter().getId().equals(recruiter.getId()) && !recruiter.getRole().name().equals("ADMIN")) {
            throw new IllegalArgumentException("Access denied: You do not own this job posting");
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        if (request.getJobType() != null) job.setJobType(request.getJobType());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        if (request.getExperienceMinYears() != null) job.setExperienceMinYears(request.getExperienceMinYears());
        job.setEducationRequirement(request.getEducationRequirement());
        job.setResponsibilities(request.getResponsibilities());
        job.setBenefits(request.getBenefits());
        job.setDeadline(request.getDeadline());
        if (request.getRequiredSkillNames() != null) job.setRequiredSkills(request.getRequiredSkillNames());

        Job saved = jobRepository.save(job);
        return mapToDto(saved);
    }

    @Transactional
    public void deleteJob(UserPrincipal principal, Long jobId) {
        User recruiter = userService.getCurrentUser(principal);
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + jobId));

        if (!job.getRecruiter().getId().equals(recruiter.getId()) && !recruiter.getRole().name().equals("ADMIN")) {
            throw new IllegalArgumentException("Access denied: You do not own this job posting");
        }

        job.setIsActive(false); // soft-deactivate
        jobRepository.save(job);
    }

    @Transactional(readOnly = true)
    public List<JobDto> getRecruiterJobs(UserPrincipal principal) {
        User recruiter = userService.getCurrentUser(principal);
        return jobRepository.findByRecruiter(recruiter).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<JobDto> searchJobs(String keyword, String location, String jobType, int page, int size) {
        JobType typeEnum = null;
        if (jobType != null && !jobType.isBlank() && !jobType.equalsIgnoreCase("ALL")) {
            try {
                typeEnum = JobType.valueOf(jobType.toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Job> jobPage = jobRepository.searchJobs(
                keyword != null && !keyword.isBlank() ? keyword : null,
                location != null && !location.isBlank() ? location : null,
                typeEnum,
                pageable
        );

        return jobPage.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public JobDto getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + id));
        return mapToDto(job);
    }

    public JobDto mapToDto(Job job) {
        Long appCount = applicationRepository.countByJob(job);
        return JobDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .companyName(job.getCompanyName())
                .location(job.getLocation())
                .jobType(job.getJobType())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .experienceMinYears(job.getExperienceMinYears())
                .educationRequirement(job.getEducationRequirement())
                .responsibilities(job.getResponsibilities())
                .benefits(job.getBenefits())
                .deadline(job.getDeadline())
                .requiredSkills(job.getRequiredSkills())
                .applicationCount(appCount)
                .isActive(job.getIsActive())
                .createdAt(job.getCreatedAt())
                .build();
    }
}
