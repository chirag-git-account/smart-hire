package com.smarthire.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "saved_jobs", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "job_id"})
})
public class SavedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @CreationTimestamp
    private LocalDateTime savedAt;

    public SavedJob() {}

    public SavedJob(Long id, User user, Job job, LocalDateTime savedAt) {
        this.id = id;
        this.user = user;
        this.job = job;
        this.savedAt = savedAt;
    }

    public static SavedJobBuilder builder() {
        return new SavedJobBuilder();
    }

    public static class SavedJobBuilder {
        private Long id;
        private User user;
        private Job job;
        private LocalDateTime savedAt;

        public SavedJobBuilder id(Long id) { this.id = id; return this; }
        public SavedJobBuilder user(User user) { this.user = user; return this; }
        public SavedJobBuilder job(Job job) { this.job = job; return this; }
        public SavedJobBuilder savedAt(LocalDateTime savedAt) { this.savedAt = savedAt; return this; }

        public SavedJob build() {
            return new SavedJob(id, user, job, savedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }

    public LocalDateTime getSavedAt() { return savedAt; }
    public void setSavedAt(LocalDateTime savedAt) { this.savedAt = savedAt; }
}
