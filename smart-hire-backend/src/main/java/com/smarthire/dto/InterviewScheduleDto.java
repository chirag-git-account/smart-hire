package com.smarthire.dto;

import java.time.LocalDateTime;

public class InterviewScheduleDto {
    private Long id;
    private Long applicationId;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String candidateName;
    private String candidateEmail;
    private LocalDateTime scheduledAt;
    private Integer durationMinutes;
    private String mode;
    private String meetingLink;
    private String status;
    private String feedback;

    public InterviewScheduleDto() {}

    public InterviewScheduleDto(Long id, Long applicationId, Long jobId, String jobTitle, String companyName,
                                String candidateName, String candidateEmail, LocalDateTime scheduledAt,
                                Integer durationMinutes, String mode, String meetingLink, String status, String feedback) {
        this.id = id;
        this.applicationId = applicationId;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.candidateName = candidateName;
        this.candidateEmail = candidateEmail;
        this.scheduledAt = scheduledAt;
        this.durationMinutes = durationMinutes;
        this.mode = mode;
        this.meetingLink = meetingLink;
        this.status = status;
        this.feedback = feedback;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long applicationId;
        private Long jobId;
        private String jobTitle;
        private String companyName;
        private String candidateName;
        private String candidateEmail;
        private LocalDateTime scheduledAt;
        private Integer durationMinutes;
        private String mode;
        private String meetingLink;
        private String status;
        private String feedback;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder applicationId(Long applicationId) { this.applicationId = applicationId; return this; }
        public Builder jobId(Long jobId) { this.jobId = jobId; return this; }
        public Builder jobTitle(String jobTitle) { this.jobTitle = jobTitle; return this; }
        public Builder companyName(String companyName) { this.companyName = companyName; return this; }
        public Builder candidateName(String candidateName) { this.candidateName = candidateName; return this; }
        public Builder candidateEmail(String candidateEmail) { this.candidateEmail = candidateEmail; return this; }
        public Builder scheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; return this; }
        public Builder durationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; return this; }
        public Builder mode(String mode) { this.mode = mode; return this; }
        public Builder meetingLink(String meetingLink) { this.meetingLink = meetingLink; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder feedback(String feedback) { this.feedback = feedback; return this; }

        public InterviewScheduleDto build() {
            return new InterviewScheduleDto(id, applicationId, jobId, jobTitle, companyName, candidateName, candidateEmail, scheduledAt, durationMinutes, mode, meetingLink, status, feedback);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }
    public String getCandidateEmail() { return candidateEmail; }
    public void setCandidateEmail(String candidateEmail) { this.candidateEmail = candidateEmail; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
