package com.smarthire.dto;

import com.smarthire.entity.enums.JobType;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class CreateJobRequest {
    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    private JobType jobType = JobType.FULL_TIME;
    private Long salaryMin;
    private Long salaryMax;
    private Integer experienceMinYears = 0;
    private String educationRequirement;
    private String responsibilities;
    private String benefits;
    private String deadline;
    private List<String> requiredSkillNames = new ArrayList<>();

    public CreateJobRequest() {}

    public CreateJobRequest(String title, String description, String location, JobType jobType, Long salaryMin, Long salaryMax, Integer experienceMinYears, String educationRequirement, String responsibilities, String benefits, String deadline, List<String> requiredSkillNames) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.jobType = jobType != null ? jobType : JobType.FULL_TIME;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.experienceMinYears = experienceMinYears != null ? experienceMinYears : 0;
        this.educationRequirement = educationRequirement;
        this.responsibilities = responsibilities;
        this.benefits = benefits;
        this.deadline = deadline;
        this.requiredSkillNames = requiredSkillNames != null ? requiredSkillNames : new ArrayList<>();
    }

    public static CreateJobRequestBuilder builder() {
        return new CreateJobRequestBuilder();
    }

    public static class CreateJobRequestBuilder {
        private String title;
        private String description;
        private String location;
        private JobType jobType = JobType.FULL_TIME;
        private Long salaryMin;
        private Long salaryMax;
        private Integer experienceMinYears = 0;
        private String educationRequirement;
        private String responsibilities;
        private String benefits;
        private String deadline;
        private List<String> requiredSkillNames = new ArrayList<>();

        public CreateJobRequestBuilder title(String title) { this.title = title; return this; }
        public CreateJobRequestBuilder description(String description) { this.description = description; return this; }
        public CreateJobRequestBuilder location(String location) { this.location = location; return this; }
        public CreateJobRequestBuilder jobType(JobType jobType) { this.jobType = jobType; return this; }
        public CreateJobRequestBuilder salaryMin(Long salaryMin) { this.salaryMin = salaryMin; return this; }
        public CreateJobRequestBuilder salaryMax(Long salaryMax) { this.salaryMax = salaryMax; return this; }
        public CreateJobRequestBuilder experienceMinYears(Integer experienceMinYears) { this.experienceMinYears = experienceMinYears; return this; }
        public CreateJobRequestBuilder educationRequirement(String educationRequirement) { this.educationRequirement = educationRequirement; return this; }
        public CreateJobRequestBuilder responsibilities(String responsibilities) { this.responsibilities = responsibilities; return this; }
        public CreateJobRequestBuilder benefits(String benefits) { this.benefits = benefits; return this; }
        public CreateJobRequestBuilder deadline(String deadline) { this.deadline = deadline; return this; }
        public CreateJobRequestBuilder requiredSkillNames(List<String> requiredSkillNames) { this.requiredSkillNames = requiredSkillNames; return this; }

        public CreateJobRequest build() {
            return new CreateJobRequest(title, description, location, jobType, salaryMin, salaryMax, experienceMinYears, educationRequirement, responsibilities, benefits, deadline, requiredSkillNames);
        }
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

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

    public List<String> getRequiredSkillNames() { return requiredSkillNames; }
    public void setRequiredSkillNames(List<String> requiredSkillNames) { this.requiredSkillNames = requiredSkillNames; }
}
