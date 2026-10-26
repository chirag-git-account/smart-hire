package com.smarthire.dto;

import com.smarthire.entity.enums.JobType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JobDto {
    private Long id;
    private String title;
    private String description;
    private String companyName;
    private String location;
    private JobType jobType;
    private Long salaryMin;
    private Long salaryMax;
    private Integer experienceMinYears;
    private String educationRequirement;
    private String responsibilities;
    private String benefits;
    private String deadline;
    private List<String> requiredSkills;
    private Long applicationCount;
    private Boolean isActive;
    private LocalDateTime createdAt;

    public JobDto() {}

    public JobDto(Long id, String title, String description, String companyName, String location, JobType jobType, Long salaryMin, Long salaryMax, Integer experienceMinYears, String educationRequirement, String responsibilities, String benefits, String deadline, List<String> requiredSkills, Long applicationCount, Boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.companyName = companyName;
        this.location = location;
        this.jobType = jobType;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.experienceMinYears = experienceMinYears;
        this.educationRequirement = educationRequirement;
        this.responsibilities = responsibilities;
        this.benefits = benefits;
        this.deadline = deadline;
        this.requiredSkills = requiredSkills != null ? requiredSkills : new ArrayList<>();
        this.applicationCount = applicationCount;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public static JobDtoBuilder builder() {
        return new JobDtoBuilder();
    }

    public static class JobDtoBuilder {
        private Long id;
        private String title;
        private String description;
        private String companyName;
        private String location;
        private JobType jobType;
        private Long salaryMin;
        private Long salaryMax;
        private Integer experienceMinYears;
        private String educationRequirement;
        private String responsibilities;
        private String benefits;
        private String deadline;
        private List<String> requiredSkills = new ArrayList<>();
        private Long applicationCount;
        private Boolean isActive;
        private LocalDateTime createdAt;

        public JobDtoBuilder id(Long id) { this.id = id; return this; }
        public JobDtoBuilder title(String title) { this.title = title; return this; }
        public JobDtoBuilder description(String description) { this.description = description; return this; }
        public JobDtoBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public JobDtoBuilder location(String location) { this.location = location; return this; }
        public JobDtoBuilder jobType(JobType jobType) { this.jobType = jobType; return this; }
        public JobDtoBuilder salaryMin(Long salaryMin) { this.salaryMin = salaryMin; return this; }
        public JobDtoBuilder salaryMax(Long salaryMax) { this.salaryMax = salaryMax; return this; }
        public JobDtoBuilder experienceMinYears(Integer experienceMinYears) { this.experienceMinYears = experienceMinYears; return this; }
        public JobDtoBuilder educationRequirement(String educationRequirement) { this.educationRequirement = educationRequirement; return this; }
        public JobDtoBuilder responsibilities(String responsibilities) { this.responsibilities = responsibilities; return this; }
        public JobDtoBuilder benefits(String benefits) { this.benefits = benefits; return this; }
        public JobDtoBuilder deadline(String deadline) { this.deadline = deadline; return this; }
        public JobDtoBuilder requiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; return this; }
        public JobDtoBuilder applicationCount(Long applicationCount) { this.applicationCount = applicationCount; return this; }
        public JobDtoBuilder isActive(Boolean isActive) { this.isActive = isActive; return this; }
        public JobDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public JobDto build() {
            return new JobDto(id, title, description, companyName, location, jobType, salaryMin, salaryMax, experienceMinYears, educationRequirement, responsibilities, benefits, deadline, requiredSkills, applicationCount, isActive, createdAt);
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

    public Long getApplicationCount() { return applicationCount; }
    public void setApplicationCount(Long applicationCount) { this.applicationCount = applicationCount; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
