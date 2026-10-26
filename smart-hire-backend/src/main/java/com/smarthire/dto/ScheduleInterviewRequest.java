package com.smarthire.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ScheduleInterviewRequest {
    @NotNull(message = "Application ID is required")
    private Long applicationId;

    @NotBlank(message = "Scheduled datetime is required")
    private String scheduledAt;

    private Integer durationMinutes = 45;
    private String mode = "ONLINE";
    private String meetingLink;
    private String feedback;

    public ScheduleInterviewRequest() {}

    public ScheduleInterviewRequest(Long applicationId, String scheduledAt, Integer durationMinutes, String mode, String meetingLink, String feedback) {
        this.applicationId = applicationId;
        this.scheduledAt = scheduledAt;
        this.durationMinutes = durationMinutes != null ? durationMinutes : 45;
        this.mode = mode != null ? mode : "ONLINE";
        this.meetingLink = meetingLink;
        this.feedback = feedback;
    }

    public static ScheduleInterviewRequestBuilder builder() {
        return new ScheduleInterviewRequestBuilder();
    }

    public static class ScheduleInterviewRequestBuilder {
        private Long applicationId;
        private String scheduledAt;
        private Integer durationMinutes = 45;
        private String mode = "ONLINE";
        private String meetingLink;
        private String feedback;

        public ScheduleInterviewRequestBuilder applicationId(Long applicationId) { this.applicationId = applicationId; return this; }
        public ScheduleInterviewRequestBuilder scheduledAt(String scheduledAt) { this.scheduledAt = scheduledAt; return this; }
        public ScheduleInterviewRequestBuilder durationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; return this; }
        public ScheduleInterviewRequestBuilder mode(String mode) { this.mode = mode; return this; }
        public ScheduleInterviewRequestBuilder meetingLink(String meetingLink) { this.meetingLink = meetingLink; return this; }
        public ScheduleInterviewRequestBuilder feedback(String feedback) { this.feedback = feedback; return this; }

        public ScheduleInterviewRequest build() {
            return new ScheduleInterviewRequest(applicationId, scheduledAt, durationMinutes, mode, meetingLink, feedback);
        }
    }

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }

    public String getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(String scheduledAt) { this.scheduledAt = scheduledAt; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
