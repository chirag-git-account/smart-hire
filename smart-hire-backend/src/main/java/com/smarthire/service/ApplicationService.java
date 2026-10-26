package com.smarthire.service;

import com.smarthire.dto.ApplicationDto;
import com.smarthire.dto.ApplyJobRequest;
import com.smarthire.dto.CandidateRankingDto;
import com.smarthire.entity.*;
import com.smarthire.entity.enums.ApplicationStatus;
import com.smarthire.entity.enums.NotificationType;
import com.smarthire.repository.*;
import com.smarthire.security.UserPrincipal;
import com.smarthire.service.ai.AiCandidateAnalysisService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final ResumeRepository resumeRepository;
    private final UserService userService;
    private final AiCandidateAnalysisService aiCandidateAnalysisService;
    private final NotificationService notificationService;

    public ApplicationService(ApplicationRepository applicationRepository,
                              JobRepository jobRepository,
                              ResumeRepository resumeRepository,
                              UserService userService,
                              AiCandidateAnalysisService aiCandidateAnalysisService,
                              NotificationService notificationService) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.resumeRepository = resumeRepository;
        this.userService = userService;
        this.aiCandidateAnalysisService = aiCandidateAnalysisService;
        this.notificationService = notificationService;
    }

    @Transactional
    public ApplicationDto applyForJob(UserPrincipal principal, ApplyJobRequest request) {
        User candidate = userService.getCurrentUser(principal);

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + request.getJobId()));

        if (applicationRepository.existsByJobAndCandidate(job, candidate)) {
            throw new IllegalStateException("You have already applied for this job opening.");
        }

        Resume resume = null;
        if (request.getResumeId() != null) {
            resume = resumeRepository.findById(request.getResumeId()).orElse(null);
        }
        if (resume == null) {
            resume = resumeRepository.findFirstByUserAndIsPrimaryTrue(candidate)
                    .orElseGet(() -> {
                        List<Resume> list = resumeRepository.findByUserOrderByCreatedAtDesc(candidate);
                        return list.isEmpty() ? null : list.get(0);
                    });
        }
        if (resume == null) {
            throw new IllegalArgumentException("No resume found on file. Please upload a resume before applying.");
        }

        Application application = Application.builder()
                .job(job)
                .candidate(candidate)
                .resume(resume)
                .coverLetter(request.getCoverLetter() != null ? request.getCoverLetter() : request.getCoverNote())
                .status(ApplicationStatus.APPLIED)
                .build();

        Application saved = applicationRepository.save(application);

        // Run AI Analysis immediately on application submission
        AiAnalysis analysis = aiCandidateAnalysisService.generateOrUpdateAnalysis(saved);
        saved.setAiAnalysis(analysis);

        // Send In-App Notifications
        if (job.getRecruiter() != null) {
            notificationService.sendNotification(
                    job.getRecruiter(),
                    "New Application Received",
                    candidate.getFullName() + " applied for " + job.getTitle() + " (AI Score: " + (analysis != null ? analysis.getOverallScore() : "Pending") + "/100)",
                    NotificationType.APPLICATION_SUBMITTED,
                    "/candidate-analysis/" + saved.getId()
            );
        }

        notificationService.sendNotification(
                candidate,
                "Application Submitted Successfully",
                "Your application for '" + job.getTitle() + "' at " + job.getCompanyName() + " was submitted.",
                NotificationType.APPLICATION_SUBMITTED,
                "/dashboard/seeker"
        );

        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<ApplicationDto> getMyApplications(UserPrincipal principal) {
        User candidate = userService.getCurrentUser(principal);
        return applicationRepository.findByCandidateOrderByAppliedAtDesc(candidate).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CandidateRankingDto> getCandidatesForJob(UserPrincipal principal, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + jobId));

        List<Application> applications = applicationRepository.findByJob(job);

        return applications.stream()
                .map(app -> {
                    AiAnalysis analysis = app.getAiAnalysis();
                    if (analysis == null) {
                        analysis = aiCandidateAnalysisService.generateOrUpdateAnalysis(app);
                    }

                    return CandidateRankingDto.builder()
                            .id(app.getId())
                            .applicationId(app.getId())
                            .candidateName(app.getCandidate().getFullName())
                            .candidateEmail(app.getCandidate().getEmail())
                            .overallScore(analysis != null ? analysis.getOverallScore() : 0)
                            .skillsScore(analysis != null ? analysis.getSkillsScore() : 0)
                            .experienceScore(analysis != null ? analysis.getExperienceScore() : 0)
                            .recommendation(analysis != null ? analysis.getRecommendation() : null)
                            .matchedSkills(analysis != null ? analysis.getMatchedSkills() : List.of())
                            .missingSkills(analysis != null ? analysis.getMissingSkills() : List.of())
                            .applicationStatus(app.getStatus().name())
                            .resumeId(app.getResume() != null ? app.getResume().getId() : null)
                            .resumeFileName(app.getResume() != null ? app.getResume().getFileName() : "Resume.pdf")
                            .build();
                })
                .sorted(Comparator.comparing(CandidateRankingDto::getOverallScore, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());
    }

    @Transactional
    public ApplicationDto updateStatus(Long applicationId, ApplicationStatus newStatus) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with id: " + applicationId));

        app.setStatus(newStatus);
        Application saved = applicationRepository.save(app);

        // Notify Candidate
        notificationService.sendNotification(
                app.getCandidate(),
                "Application Status Update",
                "Your application for '" + app.getJob().getTitle() + "' was updated to " + newStatus,
                NotificationType.APPLICATION_STATUS_CHANGED,
                "/dashboard/seeker"
        );

        return mapToDto(saved);
    }

    public ApplicationDto mapToDto(Application app) {
        AiAnalysis analysis = app.getAiAnalysis();

        return ApplicationDto.builder()
                .id(app.getId())
                .jobId(app.getJob().getId())
                .jobTitle(app.getJob().getTitle())
                .companyName(app.getJob().getCompanyName())
                .candidateId(app.getCandidate().getId())
                .candidateName(app.getCandidate().getFullName())
                .resumeId(app.getResume() != null ? app.getResume().getId() : null)
                .resumeFileName(app.getResume() != null ? app.getResume().getFileName() : null)
                .coverLetter(app.getCoverLetter())
                .status(app.getStatus())
                .appliedAt(app.getAppliedAt())
                .aiScore(analysis != null ? analysis.getOverallScore() : null)
                .aiRecommendation(analysis != null ? analysis.getRecommendation() : null)
                .build();
    }
}
