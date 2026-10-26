package com.smarthire.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    private String contentType;

    private Long fileSizeBytes;

    @Column(columnDefinition = "LONGTEXT")
    private String extractedText;

    private Double parsedExperienceYears = 0.0;

    private Boolean isPrimary = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Resume() {}

    public Resume(Long id, User user, String fileName, String filePath, String contentType, Long fileSizeBytes, String extractedText, Double parsedExperienceYears, Boolean isPrimary, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.fileName = fileName;
        this.filePath = filePath;
        this.contentType = contentType;
        this.fileSizeBytes = fileSizeBytes;
        this.extractedText = extractedText;
        this.parsedExperienceYears = parsedExperienceYears != null ? parsedExperienceYears : 0.0;
        this.isPrimary = isPrimary != null ? isPrimary : true;
        this.createdAt = createdAt;
    }

    public static ResumeBuilder builder() {
        return new ResumeBuilder();
    }

    public static class ResumeBuilder {
        private Long id;
        private User user;
        private String fileName;
        private String filePath;
        private String contentType;
        private Long fileSizeBytes;
        private String extractedText;
        private Double parsedExperienceYears = 0.0;
        private Boolean isPrimary = true;
        private LocalDateTime createdAt;

        public ResumeBuilder id(Long id) { this.id = id; return this; }
        public ResumeBuilder user(User user) { this.user = user; return this; }
        public ResumeBuilder fileName(String fileName) { this.fileName = fileName; return this; }
        public ResumeBuilder filePath(String filePath) { this.filePath = filePath; return this; }
        public ResumeBuilder contentType(String contentType) { this.contentType = contentType; return this; }
        public ResumeBuilder fileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; return this; }
        public ResumeBuilder extractedText(String extractedText) { this.extractedText = extractedText; return this; }
        public ResumeBuilder parsedExperienceYears(Double parsedExperienceYears) { this.parsedExperienceYears = parsedExperienceYears; return this; }
        public ResumeBuilder isPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; return this; }
        public ResumeBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Resume build() {
            return new Resume(id, user, fileName, filePath, contentType, fileSizeBytes, extractedText, parsedExperienceYears, isPrimary, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public Long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }

    public String getExtractedText() { return extractedText; }
    public void setExtractedText(String extractedText) { this.extractedText = extractedText; }

    public Double getParsedExperienceYears() { return parsedExperienceYears; }
    public void setParsedExperienceYears(Double parsedExperienceYears) { this.parsedExperienceYears = parsedExperienceYears; }

    public Boolean getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Boolean primary) { isPrimary = primary; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
