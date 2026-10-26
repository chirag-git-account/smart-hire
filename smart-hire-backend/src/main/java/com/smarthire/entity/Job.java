package com.smarthire.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smarthire.entity.enums.JobType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType = JobType.FULL_TIME;

    private Long salaryMin;
    private Long salaryMax;

    private Integer experienceMinYears = 0;

    private String educationRequirement;

    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @Column(columnDefinition = "TEXT")
    private String benefits;

    private String deadline;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "job_required_skills", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "skill_name")
    private List<String> requiredSkills = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    @JsonIgnore
    private User recruiter;

    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Job() {}

    public Job(Long id, String title, String description, String companyName, String location, JobType jobType, Long salaryMin, Long salaryMax, Integer experienceMinYears, String educationRequirement, String responsibilities, String benefits, String deadline, List<String> requiredSkills, User recruiter, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.companyName = companyName;
        this.location = location;
        this.jobType = jobType != null ? jobType : JobType.FULL_TIME;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.experienceMinYears = experienceMinYears != null ? experienceMinYears : 0;
        this.educationRequirement = educationRequirement;
        this.responsibilities = responsibilities;
        this.benefits = benefits;
        this.deadline = deadline;
        this.requiredSkills = requiredSkills != null ? requiredSkills : new ArrayList<>();
        this.recruiter = recruiter;
        this.isActive = isActive != null ? isActive : true;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static JobBuilder builder() {
        return new JobBuilder();
    }

    public static class JobBuilder {
        private Long id;
        private String title;
        private String description;
        private String companyName;
        private String location;
        private JobType jobType = JobType.FULL_TIME;
        private Long salaryMin;
        private Long salaryMax;
        private Integer experienceMinYears = 0;
        private String educationRequirement;
        private String responsibilities;
        private String benefits;
        private String deadline;
        private List<String> requiredSkills = new ArrayList<>();
        private User recruiter;
        private Boolean isActive = true;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public JobBuilder id(Long id) { this.id = id; return this; }
        public JobBuilder title(String title) { this.title = title; return this; }
        public JobBuilder description(String description) { this.description = description; return this; }
        public JobBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public JobBuilder location(String location) { this.location = location; return this; }
        public JobBuilder jobType(JobType jobType) { this.jobType = jobType; return this; }
        public JobBuilder salaryMin(Long salaryMin) { this.salaryMin = salaryMin; return this; }
        public JobBuilder salaryMax(Long salaryMax) { this.salaryMax = salaryMax; return this; }
        public JobBuilder experienceMinYears(Integer experienceMinYears) { this.experienceMinYears = experienceMinYears; return this; }
        public JobBuilder educationRequirement(String educationRequirement) { this.educationRequirement = educationRequirement; return this; }
        public JobBuilder responsibilities(String responsibilities) { this.responsibilities = responsibilities; return this; }
        public JobBuilder benefits(String benefits) { this.benefits = benefits; return this; }
        public JobBuilder deadline(String deadline) { this.deadline = deadline; return this; }
        public JobBuilder requiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; return this; }
        public JobBuilder recruiter(User recruiter) { this.recruiter = recruiter; return this; }
        public JobBuilder isActive(Boolean isActive) { this.isActive = isActive; return this; }
        public JobBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public JobBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Job build() {
            return new Job(id, title, description, companyName, location, jobType, salaryMin, salaryMax, experienceMinYears, educationRequirement, responsibilities, benefits, deadline, requiredSkills, recruiter, isActive, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public JobType getJobType() { return jobType; }
    public void setJobType(JobType jobType) { this.jobType = jobType; }

    public Long getSalaryMin() { return salaryMin; }
    public void setSalaryMin(Long salaryMin) { this.salaryMin = salaryMin; }

    public Long getSalaryMax() { return salaryMax; }
    public void setSalaryMax(Long salaryMax) { this.salaryMax = salaryMax; }

    public Integer getExperienceMinYears() { return experienceMinYears; }
    public void setExperienceMinYears(Integer experienceMinYears) { this.experienceMinYears = experienceMinYears; }

    public String getEducationRequirement() { return educationRequirement; }
    public void setEducationRequirement(String educationRequirement) { this.educationRequirement = educationRequirement; }

    public String getResponsibilities() { return responsibilities; }
    public void setResponsibilities(String responsibilities) { this.responsibilities = responsibilities; }

    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }

    public User getRecruiter() { return recruiter; }
    public void setRecruiter(User recruiter) { this.recruiter = recruiter; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
