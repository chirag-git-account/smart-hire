package com.smarthire.dto;

import java.util.List;

public class CompanyDto {
    private Long id;
    private String name;
    private String logoUrl;
    private String description;
    private String industry;
    private String location;
    private String website;
    private String employeeSize;
    private Integer foundedYear;
    private Long ownerId;
    private String ownerName;
    private String ownerEmail;
    private Boolean isActive;
    private Integer activeJobCount;
    private Integer totalJobCount;
    private List<JobDto> activeJobs;

    public CompanyDto() {}

    public CompanyDto(Long id, String name, String logoUrl, String description, String industry, String location,
                      String website, String employeeSize, Integer foundedYear, Long ownerId, String ownerName,
                      String ownerEmail, Boolean isActive, Integer activeJobCount, Integer totalJobCount, List<JobDto> activeJobs) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.description = description;
        this.industry = industry;
        this.location = location;
        this.website = website;
        this.employeeSize = employeeSize;
        this.foundedYear = foundedYear;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.isActive = isActive != null ? isActive : true;
        this.activeJobCount = activeJobCount;
        this.totalJobCount = totalJobCount;
        this.activeJobs = activeJobs;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private String logoUrl;
        private String description;
        private String industry;
        private String location;
        private String website;
        private String employeeSize;
        private Integer foundedYear;
        private Long ownerId;
        private String ownerName;
        private String ownerEmail;
        private Boolean isActive = true;
        private Integer activeJobCount;
        private Integer totalJobCount;
        private List<JobDto> activeJobs;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder logoUrl(String logoUrl) { this.logoUrl = logoUrl; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder industry(String industry) { this.industry = industry; return this; }
        public Builder location(String location) { this.location = location; return this; }
        public Builder website(String website) { this.website = website; return this; }
        public Builder employeeSize(String employeeSize) { this.employeeSize = employeeSize; return this; }
        public Builder foundedYear(Integer foundedYear) { this.foundedYear = foundedYear; return this; }
        public Builder ownerId(Long ownerId) { this.ownerId = ownerId; return this; }
        public Builder ownerName(String ownerName) { this.ownerName = ownerName; return this; }
        public Builder ownerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }
        public Builder activeJobCount(Integer activeJobCount) { this.activeJobCount = activeJobCount; return this; }
        public Builder totalJobCount(Integer totalJobCount) { this.totalJobCount = totalJobCount; return this; }
        public Builder activeJobs(List<JobDto> activeJobs) { this.activeJobs = activeJobs; return this; }

        public CompanyDto build() {
            return new CompanyDto(id, name, logoUrl, description, industry, location, website, employeeSize, foundedYear, ownerId, ownerName, ownerEmail, isActive, activeJobCount, totalJobCount, activeJobs);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public String getEmployeeSize() { return employeeSize; }
    public void setEmployeeSize(String employeeSize) { this.employeeSize = employeeSize; }
    public Integer getFoundedYear() { return foundedYear; }
    public void setFoundedYear(Integer foundedYear) { this.foundedYear = foundedYear; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }
    public Integer getActiveJobCount() { return activeJobCount; }
    public void setActiveJobCount(Integer activeJobCount) { this.activeJobCount = activeJobCount; }
    public Integer getTotalJobCount() { return totalJobCount; }
    public void setTotalJobCount(Integer totalJobCount) { this.totalJobCount = totalJobCount; }
    public List<JobDto> getActiveJobs() { return activeJobs; }
    public void setActiveJobs(List<JobDto> activeJobs) { this.activeJobs = activeJobs; }
}
