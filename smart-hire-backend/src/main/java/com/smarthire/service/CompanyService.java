package com.smarthire.service;

import com.smarthire.dto.CompanyDto;
import com.smarthire.dto.JobDto;
import com.smarthire.entity.Company;
import com.smarthire.entity.Job;
import com.smarthire.entity.User;
import com.smarthire.entity.enums.Role;
import com.smarthire.repository.CompanyRepository;
import com.smarthire.repository.JobRepository;
import com.smarthire.repository.UserRepository;
import com.smarthire.security.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, JobRepository jobRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findAll().stream()
                .filter(c -> c.getIsActive() == null || c.getIsActive())
                .map(this::mapToDtoWithJobs)
                .collect(Collectors.toList());
    }

    public CompanyDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + id));
        return mapToDtoWithJobs(company);
    }

    public CompanyDto getCompanyByName(String name) {
        Company company = companyRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with name: " + name));
        return mapToDtoWithJobs(company);
    }

    @Transactional
    public CompanyDto createOrUpdateCompany(UserPrincipal principal, CompanyDto dto) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getRole() == Role.JOB_SEEKER) {
            throw new AccessDeniedException("Candidates are not permitted to register or modify companies.");
        }

        Company company = companyRepository.findByNameIgnoreCase(dto.getName())
                .orElse(null);

        if (company == null) {
            company = Company.builder()
                    .name(dto.getName().trim())
                    .owner(user)
                    .isActive(true)
                    .build();
        } else {
            // Check ownership if recruiter
            if (user.getRole() == Role.RECRUITER && company.getOwner() != null) {
                if (!company.getOwner().getId().equals(user.getId()) && !company.getName().equalsIgnoreCase(user.getCompanyName())) {
                    throw new AccessDeniedException("You do not have permission to modify another recruiter's company profile.");
                }
            }
        }

        company.setDescription(dto.getDescription());
        company.setIndustry(dto.getIndustry());
        company.setLocation(dto.getLocation());
        company.setWebsite(dto.getWebsite());
        company.setEmployeeSize(dto.getEmployeeSize());
        company.setFoundedYear(dto.getFoundedYear());
        company.setLogoUrl(dto.getLogoUrl());

        Company saved = companyRepository.save(company);

        // Link recruiter to company
        if (user.getRole() == Role.RECRUITER) {
            user.setCompanyName(saved.getName());
            userRepository.save(user);
        }

        return mapToDtoWithJobs(saved);
    }

    private CompanyDto mapToDtoWithJobs(Company company) {
        List<Job> activeJobs = jobRepository.findByCompanyNameIgnoreCaseAndIsActiveTrueOrderByCreatedAtDesc(company.getName());
        List<JobDto> jobDtos = activeJobs.stream().map(this::mapJobToDto).collect(Collectors.toList());

        return CompanyDto.builder()
                .id(company.getId())
                .name(company.getName())
                .logoUrl(company.getLogoUrl())
                .description(company.getDescription())
                .industry(company.getIndustry())
                .location(company.getLocation())
                .website(company.getWebsite())
                .employeeSize(company.getEmployeeSize())
                .foundedYear(company.getFoundedYear())
                .ownerId(company.getOwner() != null ? company.getOwner().getId() : null)
                .ownerName(company.getOwner() != null ? company.getOwner().getFullName() : "System Managed")
                .ownerEmail(company.getOwner() != null ? company.getOwner().getEmail() : "admin@smarthire.ai")
                .isActive(company.getIsActive() != null ? company.getIsActive() : true)
                .activeJobCount(activeJobs.size())
                .totalJobCount(activeJobs.size())
                .activeJobs(jobDtos)
                .build();
    }

    private JobDto mapJobToDto(Job job) {
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
                .isActive(job.getIsActive())
                .createdAt(job.getCreatedAt())
                .build();
    }
}
