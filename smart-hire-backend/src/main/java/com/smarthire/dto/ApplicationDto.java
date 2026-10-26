package com.smarthire.dto;

import com.smarthire.entity.enums.ApplicationStatus;
import com.smarthire.entity.enums.RecommendationType;

import java.time.LocalDateTime;

public class ApplicationDto {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private Long candidateId;
    private String candidateName;
    private Long resumeId;
    private String resumeFileName;
    private String coverLetter;
    private ApplicationStatus status;
    private Integer aiScore;
    private RecommendationType aiRecommendation;
    private LocalDateTime appliedAt;

    public ApplicationDto() {}

    public ApplicationDto(Long id, Long jobId, String jobTitle, String companyName, Long candidateId, String candidateName, Long resumeId, String resumeFileName, String coverLetter, ApplicationStatus status, Integer aiScore, RecommendationType aiRecommendation, LocalDateTime appliedAt) {
        this.id = id;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.candidateId = candidateId;
        this.candidateName = candidateName;
        this.resumeId = resumeId;
        this.resumeFileName = resumeFileName;
        this.coverLetter = coverLetter;
        this.status = status;
        this.aiScore = aiScore;
        this.aiRecommendation = aiRecommendation;
        this.appliedAt = appliedAt;
    }

    public static ApplicationDtoBuilder builder() {
        return new ApplicationDtoBuilder();
    }

    public static class ApplicationDtoBuilder {
        private Long id;
        private Long jobId;
        private String jobTitle;
        private String companyName;
        private Long candidateId;
        private String candidateName;
        private Long resumeId;
        private String resumeFileName;
        private String coverLetter;
        private ApplicationStatus status;
        private Integer aiScore;
        private RecommendationType aiRecommendation;
        private LocalDateTime appliedAt;

        public ApplicationDtoBuilder id(Long id) { this.id = id; return this; }
        public ApplicationDtoBuilder jobId(Long jobId) { this.jobId = jobId; return this; }
        public ApplicationDtoBuilder jobTitle(String jobTitle) { this.jobTitle = jobTitle; return this; }
        public ApplicationDtoBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public ApplicationDtoBuilder candidateId(Long candidateId) { this.candidateId = candidateId; return this; }
        public ApplicationDtoBuilder candidateName(String candidateName) { this.candidateName = candidateName; return this; }
        public ApplicationDtoBuilder resumeId(Long resumeId) { this.resumeId = resumeId; return this; }
        public ApplicationDtoBuilder resumeFileName(String resumeFileName) { this.resumeFileName = resumeFileName; return this; }
        public ApplicationDtoBuilder coverLetter(String coverLetter) { this.coverLetter = coverLetter; return this; }
        public ApplicationDtoBuilder status(ApplicationStatus status) { this.status = status; return this; }
        public ApplicationDtoBuilder aiScore(Integer aiScore) { this.aiScore = aiScore; return this; }
        public ApplicationDtoBuilder aiRecommendation(RecommendationType aiRecommendation) { this.aiRecommendation = aiRecommendation; return this; }
        public ApplicationDtoBuilder appliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; return this; }

        public ApplicationDto build() {
            return new ApplicationDto(id, jobId, jobTitle, companyName, candidateId, candidateName, resumeId, resumeFileName, coverLetter, status, aiScore, aiRecommendation, appliedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public Long getCandidateId() { return candidateId; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }

    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }

    public Long getResumeId() { return resumeId; }
    public void setResumeId(Long resumeId) { this.resumeId = resumeId; }

    public String getResumeFileName() { return resumeFileName; }
    public void setResumeFileName(String resumeFileName) { this.resumeFileName = resumeFileName; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public Integer getAiScore() { return aiScore; }
    public void setAiScore(Integer aiScore) { this.aiScore = aiScore; }

    public RecommendationType getAiRecommendation() { return aiRecommendation; }
    public void setAiRecommendation(RecommendationType aiRecommendation) { this.aiRecommendation = aiRecommendation; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
}
