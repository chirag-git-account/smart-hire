package com.smarthire.dto;

import com.smarthire.entity.enums.RecommendationType;

import java.util.ArrayList;
import java.util.List;

public class CandidateRankingDto {
    private Long id;
    private Long applicationId;
    private String candidateName;
    private String candidateEmail;
    private Long resumeId;
    private String resumeFileName;
    private Integer overallScore;
    private Integer skillsScore;
    private Integer experienceScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private RecommendationType recommendation;
    private String applicationStatus;

    public CandidateRankingDto() {}

    public CandidateRankingDto(Long id, Long applicationId, String candidateName, String candidateEmail,
                               Long resumeId, String resumeFileName, Integer overallScore,
                               Integer skillsScore, Integer experienceScore, List<String> matchedSkills,
                               List<String> missingSkills, RecommendationType recommendation, String applicationStatus) {
        this.id = id;
        this.applicationId = applicationId;
        this.candidateName = candidateName;
        this.candidateEmail = candidateEmail;
        this.resumeId = resumeId;
        this.resumeFileName = resumeFileName;
        this.overallScore = overallScore;
        this.skillsScore = skillsScore;
        this.experienceScore = experienceScore;
        this.matchedSkills = matchedSkills != null ? matchedSkills : new ArrayList<>();
        this.missingSkills = missingSkills != null ? missingSkills : new ArrayList<>();
        this.recommendation = recommendation;
        this.applicationStatus = applicationStatus;
    }

    public static CandidateRankingDtoBuilder builder() {
        return new CandidateRankingDtoBuilder();
    }

    public static class CandidateRankingDtoBuilder {
        private Long id;
        private Long applicationId;
        private String candidateName;
        private String candidateEmail;
        private Long resumeId;
        private String resumeFileName;
        private Integer overallScore;
        private Integer skillsScore;
        private Integer experienceScore;
        private List<String> matchedSkills = new ArrayList<>();
        private List<String> missingSkills = new ArrayList<>();
        private RecommendationType recommendation;
        private String applicationStatus;

        public CandidateRankingDtoBuilder id(Long id) { this.id = id; return this; }
        public CandidateRankingDtoBuilder applicationId(Long applicationId) { this.applicationId = applicationId; return this; }
        public CandidateRankingDtoBuilder candidateName(String candidateName) { this.candidateName = candidateName; return this; }
        public CandidateRankingDtoBuilder candidateEmail(String candidateEmail) { this.candidateEmail = candidateEmail; return this; }
        public CandidateRankingDtoBuilder resumeId(Long resumeId) { this.resumeId = resumeId; return this; }
        public CandidateRankingDtoBuilder resumeFileName(String resumeFileName) { this.resumeFileName = resumeFileName; return this; }
        public CandidateRankingDtoBuilder overallScore(Integer overallScore) { this.overallScore = overallScore; return this; }
        public CandidateRankingDtoBuilder skillsScore(Integer skillsScore) { this.skillsScore = skillsScore; return this; }
        public CandidateRankingDtoBuilder experienceScore(Integer experienceScore) { this.experienceScore = experienceScore; return this; }
        public CandidateRankingDtoBuilder matchedSkills(List<String> matchedSkills) { this.matchedSkills = matchedSkills; return this; }
        public CandidateRankingDtoBuilder missingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; return this; }
        public CandidateRankingDtoBuilder recommendation(RecommendationType recommendation) { this.recommendation = recommendation; return this; }
        public CandidateRankingDtoBuilder applicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; return this; }

        public CandidateRankingDto build() {
            return new CandidateRankingDto(id, applicationId, candidateName, candidateEmail, resumeId, resumeFileName, overallScore, skillsScore, experienceScore, matchedSkills, missingSkills, recommendation, applicationStatus);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }
    public String getCandidateEmail() { return candidateEmail; }
    public void setCandidateEmail(String candidateEmail) { this.candidateEmail = candidateEmail; }
    public Long getResumeId() { return resumeId; }
    public void setResumeId(Long resumeId) { this.resumeId = resumeId; }
    public String getResumeFileName() { return resumeFileName; }
    public void setResumeFileName(String resumeFileName) { this.resumeFileName = resumeFileName; }
    public Integer getOverallScore() { return overallScore; }
    public void setOverallScore(Integer overallScore) { this.overallScore = overallScore; }
    public Integer getSkillsScore() { return skillsScore; }
    public void setSkillsScore(Integer skillsScore) { this.skillsScore = skillsScore; }
    public Integer getExperienceScore() { return experienceScore; }
    public void setExperienceScore(Integer experienceScore) { this.experienceScore = experienceScore; }
    public List<String> getMatchedSkills() { return matchedSkills; }
    public void setMatchedSkills(List<String> matchedSkills) { this.matchedSkills = matchedSkills; }
    public List<String> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; }
    public RecommendationType getRecommendation() { return recommendation; }
    public void setRecommendation(RecommendationType recommendation) { this.recommendation = recommendation; }
    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; }
}
