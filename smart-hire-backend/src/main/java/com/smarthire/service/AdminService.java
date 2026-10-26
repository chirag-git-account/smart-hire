package com.smarthire.service;

import com.smarthire.dto.AdminStatsDto;
import com.smarthire.dto.ApplicationDto;
import com.smarthire.dto.CompanyDto;
import com.smarthire.dto.JobDto;
import com.smarthire.dto.UserDto;
import com.smarthire.entity.Application;
import com.smarthire.entity.Company;
import com.smarthire.entity.Job;
import com.smarthire.entity.User;
import com.smarthire.entity.enums.ApplicationStatus;
import com.smarthire.entity.enums.Role;
import com.smarthire.entity.enums.UserStatus;
import com.smarthire.repository.AiAnalysisRepository;
import com.smarthire.repository.ApplicationRepository;
import com.smarthire.repository.CompanyRepository;
import com.smarthire.repository.JobRepository;
import com.smarthire.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationRepository applicationRepository;
    private final AiAnalysisRepository aiAnalysisRepository;
    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;

    public AdminService(UserRepository userRepository,
                        JobRepository jobRepository,
                        CompanyRepository companyRepository,
                        ApplicationRepository applicationRepository,
                        AiAnalysisRepository aiAnalysisRepository,
                        UserService userService,
                        JobService jobService,
                        ApplicationService applicationService) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.applicationRepository = applicationRepository;
        this.aiAnalysisRepository = aiAnalysisRepository;
        this.userService = userService;
        this.jobService = jobService;
        this.applicationService = applicationService;
    }

    @Transactional(readOnly = true)
    public AdminStatsDto getAdminStats() {
        Long totalUsers = userRepository.count();
        Long totalSeekers = userRepository.countByRole(Role.JOB_SEEKER);
        Long totalRecruiters = userRepository.countByRole(Role.RECRUITER);
        Long totalCompanies = companyRepository.count();
        Long totalJobs = jobRepository.count();
        Long totalApplications = applicationRepository.count();
        Long totalAnalyses = aiAnalysisRepository.count();

        return AdminStatsDto.builder()
                .totalUsers(totalUsers)
                .totalJobSeekers(totalSeekers)
                .totalRecruiters(totalRecruiters)
                .totalCompanies(totalCompanies)
                .totalJobs(totalJobs)
                .totalApplications(totalApplications)
                .totalAiAnalyses(totalAnalyses)
                .build();
    }

    // ==========================================
    // USER MANAGEMENT
    // ==========================================
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userService::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto updateUserStatus(Long userId, UserStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        user.setStatus(status);
        User saved = userRepository.save(user);
        return userService.mapToDto(saved);
    }

    // ==========================================
    // 1. ADMIN COMPANY MANAGEMENT
    // ==========================================
    @Transactional(readOnly = true)
    public Page<CompanyDto> getCompanies(String keyword, Boolean isActive, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        String cleanKeyword = (keyword != null && !keyword.isBlank()) ? keyword.trim() : null;

        Page<Company> companyPage = companyRepository.searchCompaniesWithStatus(cleanKeyword, isActive, pageable);
        return companyPage.map(this::mapCompanyToDto);
    }

    @Transactional(readOnly = true)
    public CompanyDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + id));
        return mapCompanyToDto(company);
    }

    @Transactional
    public CompanyDto updateCompanyStatus(Long id, Boolean active) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + id));

        company.setIsActive(active != null ? active : !company.getIsActive());
        Company saved = companyRepository.save(company);
        return mapCompanyToDto(saved);
    }

    @Transactional
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + id));

        // Soft deactivate jobs under company
        List<Job> companyJobs = jobRepository.findByCompanyNameIgnoreCase(company.getName());
        for (Job j : companyJobs) {
            j.setIsActive(false);
            jobRepository.save(j);
        }

        companyRepository.delete(company);
    }

    // ==========================================
    // 2. ADMIN JOB MANAGEMENT
    // ==========================================
    @Transactional(readOnly = true)
    public Page<JobDto> getJobs(String keyword, String company, Boolean isActive, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        String cleanKeyword = (keyword != null && !keyword.isBlank()) ? keyword.trim() : null;
        String cleanCompany = (company != null && !company.isBlank() && !company.equalsIgnoreCase("ALL")) ? company.trim() : null;

        Page<Job> jobPage = jobRepository.adminSearchJobs(cleanKeyword, cleanCompany, isActive, pageable);
        return jobPage.map(jobService::mapToDto);
    }

    @Transactional(readOnly = true)
    public JobDto getJobById(Long id) {
        return jobService.getJobById(id);
    }

    @Transactional
    public JobDto updateJobStatus(Long id, Boolean active) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + id));

        job.setIsActive(active != null ? active : !job.getIsActive());
        Job saved = jobRepository.save(job);
        return jobService.mapToDto(saved);
    }

    @Transactional
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + id));
        job.setIsActive(false);
        jobRepository.save(job);
    }

    // ==========================================
    // 3. ADMIN APPLICATION MANAGEMENT
    // ==========================================
    @Transactional(readOnly = true)
    public Page<ApplicationDto> getApplications(String keyword, ApplicationStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        String cleanKeyword = (keyword != null && !keyword.isBlank()) ? keyword.trim() : null;

        Page<Application> appPage = applicationRepository.searchApplications(status, cleanKeyword, pageable);
        return appPage.map(applicationService::mapToDto);
    }

    @Transactional(readOnly = true)
    public ApplicationDto getApplicationById(Long id) {
        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with id: " + id));
        return applicationService.mapToDto(app);
    }

    @Transactional
    public ApplicationDto updateApplicationStatus(Long id, ApplicationStatus status) {
        return applicationService.updateStatus(id, status);
    }

    private CompanyDto mapCompanyToDto(Company company) {
        List<Job> activeJobs = jobRepository.findByCompanyNameIgnoreCaseAndIsActiveTrueOrderByCreatedAtDesc(company.getName());
        Long totalCount = jobRepository.countByCompanyNameIgnoreCase(company.getName());

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
                .totalJobCount(totalCount != null ? totalCount.intValue() : activeJobs.size())
                .build();
    }
}
