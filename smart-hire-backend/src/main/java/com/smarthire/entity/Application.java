package com.smarthire.entity;

import com.smarthire.entity.enums.ApplicationStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private AiAnalysis aiAnalysis;

    @CreationTimestamp
    private LocalDateTime appliedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Application() {}

    public Application(Long id, Job job, User candidate, Resume resume, String coverLetter, ApplicationStatus status, AiAnalysis aiAnalysis, LocalDateTime appliedAt, LocalDateTime updatedAt) {
        this.id = id;
        this.job = job;
        this.candidate = candidate;
        this.resume = resume;
        this.coverLetter = coverLetter;
        this.status = status != null ? status : ApplicationStatus.APPLIED;
        this.aiAnalysis = aiAnalysis;
        this.appliedAt = appliedAt;
        this.updatedAt = updatedAt;
    }

    public static ApplicationBuilder builder() {
        return new ApplicationBuilder();
    }

    public static class ApplicationBuilder {
        private Long id;
        private Job job;
        private User candidate;
        private Resume resume;
        private String coverLetter;
        private ApplicationStatus status = ApplicationStatus.APPLIED;
        private AiAnalysis aiAnalysis;
        private LocalDateTime appliedAt;
        private LocalDateTime updatedAt;

        public ApplicationBuilder id(Long id) { this.id = id; return this; }
        public ApplicationBuilder job(Job job) { this.job = job; return this; }
        public ApplicationBuilder candidate(User candidate) { this.candidate = candidate; return this; }
        public ApplicationBuilder resume(Resume resume) { this.resume = resume; return this; }
        public ApplicationBuilder coverLetter(String coverLetter) { this.coverLetter = coverLetter; return this; }
        public ApplicationBuilder status(ApplicationStatus status) { this.status = status; return this; }
        public ApplicationBuilder aiAnalysis(AiAnalysis aiAnalysis) { this.aiAnalysis = aiAnalysis; return this; }
        public ApplicationBuilder appliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; return this; }
        public ApplicationBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Application build() {
            return new Application(id, job, candidate, resume, coverLetter, status, aiAnalysis, appliedAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }

    public User getCandidate() { return candidate; }
    public void setCandidate(User candidate) { this.candidate = candidate; }

    public Resume getResume() { return resume; }
    public void setResume(Resume resume) { this.resume = resume; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public AiAnalysis getAiAnalysis() { return aiAnalysis; }
    public void setAiAnalysis(AiAnalysis aiAnalysis) { this.aiAnalysis = aiAnalysis; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
