package com.smarthire.dto;

import java.util.ArrayList;
import java.util.List;

public class UserProfileDto {
    private String fullName;
    private String phone;
    private String headline;
    private String summary;
    private String currentLocation;
    private Double totalExperienceYears;
    private String expectedSalary;
    private Integer noticePeriodDays;
    private String education;
    private String experienceDetails;
    private String githubUrl;
    private String linkedinUrl;
    private String portfolioUrl;
    private List<String> skills;

    public UserProfileDto() {}

    public UserProfileDto(String fullName, String phone, String headline, String summary, String currentLocation,
                          Double totalExperienceYears, String expectedSalary, Integer noticePeriodDays,
                          String education, String experienceDetails, String githubUrl, String linkedinUrl,
                          String portfolioUrl, List<String> skills) {
        this.fullName = fullName;
        this.phone = phone;
        this.headline = headline;
        this.summary = summary;
        this.currentLocation = currentLocation;
        this.totalExperienceYears = totalExperienceYears;
        this.expectedSalary = expectedSalary;
        this.noticePeriodDays = noticePeriodDays;
        this.education = education;
        this.experienceDetails = experienceDetails;
        this.githubUrl = githubUrl;
        this.linkedinUrl = linkedinUrl;
        this.portfolioUrl = portfolioUrl;
        this.skills = skills != null ? skills : new ArrayList<>();
    }

    public static UserProfileDtoBuilder builder() {
        return new UserProfileDtoBuilder();
    }

    public static class UserProfileDtoBuilder {
        private String fullName;
        private String phone;
        private String headline;
        private String summary;
        private String currentLocation;
        private Double totalExperienceYears;
        private String expectedSalary;
        private Integer noticePeriodDays;
        private String education;
        private String experienceDetails;
        private String githubUrl;
        private String linkedinUrl;
        private String portfolioUrl;
        private List<String> skills = new ArrayList<>();

        public UserProfileDtoBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public UserProfileDtoBuilder phone(String phone) { this.phone = phone; return this; }
        public UserProfileDtoBuilder headline(String headline) { this.headline = headline; return this; }
        public UserProfileDtoBuilder summary(String summary) { this.summary = summary; return this; }
        public UserProfileDtoBuilder currentLocation(String currentLocation) { this.currentLocation = currentLocation; return this; }
        public UserProfileDtoBuilder totalExperienceYears(Double totalExperienceYears) { this.totalExperienceYears = totalExperienceYears; return this; }
        public UserProfileDtoBuilder expectedSalary(String expectedSalary) { this.expectedSalary = expectedSalary; return this; }
        public UserProfileDtoBuilder noticePeriodDays(Integer noticePeriodDays) { this.noticePeriodDays = noticePeriodDays; return this; }
        public UserProfileDtoBuilder education(String education) { this.education = education; return this; }
        public UserProfileDtoBuilder experienceDetails(String experienceDetails) { this.experienceDetails = experienceDetails; return this; }
        public UserProfileDtoBuilder githubUrl(String githubUrl) { this.githubUrl = githubUrl; return this; }
        public UserProfileDtoBuilder linkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; return this; }
        public UserProfileDtoBuilder portfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; return this; }
        public UserProfileDtoBuilder skills(List<String> skills) { this.skills = skills; return this; }

        public UserProfileDto build() {
            return new UserProfileDto(fullName, phone, headline, summary, currentLocation, totalExperienceYears, expectedSalary, noticePeriodDays, education, experienceDetails, githubUrl, linkedinUrl, portfolioUrl, skills);
        }
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }
    public Double getTotalExperienceYears() { return totalExperienceYears; }
    public void setTotalExperienceYears(Double totalExperienceYears) { this.totalExperienceYears = totalExperienceYears; }
    public String getExpectedSalary() { return expectedSalary; }
    public void setExpectedSalary(String expectedSalary) { this.expectedSalary = expectedSalary; }
    public Integer getNoticePeriodDays() { return noticePeriodDays; }
    public void setNoticePeriodDays(Integer noticePeriodDays) { this.noticePeriodDays = noticePeriodDays; }
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    public String getExperienceDetails() { return experienceDetails; }
    public void setExperienceDetails(String experienceDetails) { this.experienceDetails = experienceDetails; }
    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }
    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }
    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
}
