package com.smarthire.service;

import com.smarthire.dto.RecruiterStatsDto;
import com.smarthire.dto.SeekerStatsDto;
import com.smarthire.entity.User;
import com.smarthire.entity.enums.ApplicationStatus;
import com.smarthire.repository.ApplicationRepository;
import com.smarthire.repository.JobRepository;
import com.smarthire.repository.SavedJobRepository;
import com.smarthire.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final SavedJobRepository savedJobRepository;
    private final UserService userService;

    public DashboardService(ApplicationRepository applicationRepository,
                            JobRepository jobRepository,
                            SavedJobRepository savedJobRepository,
                            UserService userService) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.savedJobRepository = savedJobRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public SeekerStatsDto getSeekerStats(UserPrincipal principal) {
        User user = userService.getCurrentUser(principal);
        Long totalApps = applicationRepository.countByCandidate(user);
        Long shortlisted = applicationRepository.countByCandidateAndStatus(user, ApplicationStatus.SHORTLISTED);
        Long interviews = applicationRepository.countByCandidateAndStatus(user, ApplicationStatus.INTERVIEW_SCHEDULED);
        int savedJobsCount = savedJobRepository.findByUserOrderBySavedAtDesc(user).size();

        return SeekerStatsDto.builder()
                .totalApplications(totalApps != null ? totalApps : 0L)
                .shortlistedCount(shortlisted != null ? shortlisted : 0L)
                .interviewCount(interviews != null ? interviews : 0L)
                .savedJobsCount((long) savedJobsCount)
                .build();
    }

    @Transactional(readOnly = true)
    public RecruiterStatsDto getRecruiterStats(UserPrincipal principal) {
        User recruiter = userService.getCurrentUser(principal);
        int totalJobs = jobRepository.findByRecruiter(recruiter).size();
        Long totalApps = applicationRepository.countTotalApplicationsForRecruiter(recruiter);
        Long highMatch = applicationRepository.countHighMatchApplicationsForRecruiter(recruiter);

        return RecruiterStatsDto.builder()
                .totalJobs((long) totalJobs)
                .totalApplications(totalApps != null ? totalApps : 0L)
                .highMatchCount(highMatch != null ? highMatch : 0L)
                .build();
    }
}
