package com.smarthire.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private User user;

    private String headline;

    @Column(columnDefinition = "TEXT")
    private String summary;

    private String currentLocation;

    private Double totalExperienceYears = 0.0;

    private String expectedSalary;

    private Integer noticePeriodDays = 30;

    @Column(columnDefinition = "TEXT")
    private String education;

    @Column(columnDefinition = "TEXT")
    private String experienceDetails;

    private String githubUrl;
    private String linkedinUrl;
    private String portfolioUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_skills", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "skill_name")
    private List<String> skills = new ArrayList<>();

    public UserProfile() {}

    public UserProfile(Long id, User user, String headline, String summary, String currentLocation,
                       Double totalExperienceYears, String expectedSalary, Integer noticePeriodDays,
                       String education, String experienceDetails, String githubUrl, String linkedinUrl,
                       String portfolioUrl, List<String> skills) {
        this.id = id;
        this.user = user;
        this.headline = headline;
        this.summary = summary;
        this.currentLocation = currentLocation;
        this.totalExperienceYears = totalExperienceYears != null ? totalExperienceYears : 0.0;
        this.expectedSalary = expectedSalary;
        this.noticePeriodDays = noticePeriodDays != null ? noticePeriodDays : 30;
        this.education = education;
        this.experienceDetails = experienceDetails;
        this.githubUrl = githubUrl;
        this.linkedinUrl = linkedinUrl;
        this.portfolioUrl = portfolioUrl;
        this.skills = skills != null ? skills : new ArrayList<>();
    }

    public static UserProfileBuilder builder() {
        return new UserProfileBuilder();
    }

    public static class UserProfileBuilder {
        private Long id;
        private User user;
        private String headline;
        private String summary;
        private String currentLocation;
        private Double totalExperienceYears = 0.0;
        private String expectedSalary;
        private Integer noticePeriodDays = 30;
        private String education;
        private String experienceDetails;
        private String githubUrl;
        private String linkedinUrl;
        private String portfolioUrl;
        private List<String> skills = new ArrayList<>();

        public UserProfileBuilder id(Long id) { this.id = id; return this; }
        public UserProfileBuilder user(User user) { this.user = user; return this; }
        public UserProfileBuilder headline(String headline) { this.headline = headline; return this; }
        public UserProfileBuilder summary(String summary) { this.summary = summary; return this; }
        public UserProfileBuilder currentLocation(String currentLocation) { this.currentLocation = currentLocation; return this; }
        public UserProfileBuilder totalExperienceYears(Double totalExperienceYears) { this.totalExperienceYears = totalExperienceYears; return this; }
        public UserProfileBuilder expectedSalary(String expectedSalary) { this.expectedSalary = expectedSalary; return this; }
        public UserProfileBuilder noticePeriodDays(Integer noticePeriodDays) { this.noticePeriodDays = noticePeriodDays; return this; }
        public UserProfileBuilder education(String education) { this.education = education; return this; }
        public UserProfileBuilder experienceDetails(String experienceDetails) { this.experienceDetails = experienceDetails; return this; }
        public UserProfileBuilder githubUrl(String githubUrl) { this.githubUrl = githubUrl; return this; }
        public UserProfileBuilder linkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; return this; }
        public UserProfileBuilder portfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; return this; }
        public UserProfileBuilder skills(List<String> skills) { this.skills = skills; return this; }

        public UserProfile build() {
            return new UserProfile(id, user, headline, summary, currentLocation, totalExperienceYears, expectedSalary, noticePeriodDays, education, experienceDetails, githubUrl, linkedinUrl, portfolioUrl, skills);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
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
