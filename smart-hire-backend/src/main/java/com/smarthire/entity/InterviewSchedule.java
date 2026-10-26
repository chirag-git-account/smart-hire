package com.smarthire.entity;

import com.smarthire.entity.enums.InterviewStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview_schedules")
public class InterviewSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    private Integer durationMinutes = 45;

    private String mode = "ONLINE";

    private String meetingLink;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status = InterviewStatus.SCHEDULED;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public InterviewSchedule() {}

    public InterviewSchedule(Long id, Application application, LocalDateTime scheduledAt, Integer durationMinutes, String mode, String meetingLink, InterviewStatus status, String feedback, LocalDateTime createdAt) {
        this.id = id;
        this.application = application;
        this.scheduledAt = scheduledAt;
        this.durationMinutes = durationMinutes != null ? durationMinutes : 45;
        this.mode = mode != null ? mode : "ONLINE";
        this.meetingLink = meetingLink;
        this.status = status != null ? status : InterviewStatus.SCHEDULED;
        this.feedback = feedback;
        this.createdAt = createdAt;
    }

    public static InterviewScheduleBuilder builder() {
        return new InterviewScheduleBuilder();
    }

    public static class InterviewScheduleBuilder {
        private Long id;
        private Application application;
        private LocalDateTime scheduledAt;
        private Integer durationMinutes = 45;
        private String mode = "ONLINE";
        private String meetingLink;
        private InterviewStatus status = InterviewStatus.SCHEDULED;
        private String feedback;
        private LocalDateTime createdAt;

        public InterviewScheduleBuilder id(Long id) { this.id = id; return this; }
        public InterviewScheduleBuilder application(Application application) { this.application = application; return this; }
        public InterviewScheduleBuilder scheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; return this; }
        public InterviewScheduleBuilder durationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; return this; }
        public InterviewScheduleBuilder mode(String mode) { this.mode = mode; return this; }
        public InterviewScheduleBuilder meetingLink(String meetingLink) { this.meetingLink = meetingLink; return this; }
        public InterviewScheduleBuilder status(InterviewStatus status) { this.status = status; return this; }
        public InterviewScheduleBuilder feedback(String feedback) { this.feedback = feedback; return this; }
        public InterviewScheduleBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public InterviewSchedule build() {
            return new InterviewSchedule(id, application, scheduledAt, durationMinutes, mode, meetingLink, status, feedback, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Application getApplication() { return application; }
    public void setApplication(Application application) { this.application = application; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
    public InterviewStatus getStatus() { return status; }
    public void setStatus(InterviewStatus status) { this.status = status; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
