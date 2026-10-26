package com.smarthire.dto;

import com.smarthire.entity.enums.RecommendationType;

import java.util.ArrayList;
import java.util.List;

public class AiAnalysisDto {
    private Long id;
    private Long applicationId;
    private String candidateName;
    private String candidateEmail;
    private Long resumeId;
    private String resumeFileName;
    private String jobTitle;
    private Integer overallScore;
    private Integer skillsScore;
    private Integer experienceScore;
    private Integer jdRelevanceScore;
    private Integer educationScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private String analysisSummary;
    private List<String> strengths;
    private List<String> weaknesses;
    private RecommendationType recommendation;

    public AiAnalysisDto() {}

    public AiAnalysisDto(Long id, Long applicationId, String candidateName, String candidateEmail,
                         Long resumeId, String resumeFileName, String jobTitle, Integer overallScore,
                         Integer skillsScore, Integer experienceScore, Integer jdRelevanceScore,
                         Integer educationScore, List<String> matchedSkills, List<String> missingSkills,
                         String analysisSummary, List<String> strengths, List<String> weaknesses,
                         RecommendationType recommendation) {
        this.id = id;
        this.applicationId = applicationId;
        this.candidateName = candidateName;
        this.candidateEmail = candidateEmail;
        this.resumeId = resumeId;
        this.resumeFileName = resumeFileName;
        this.jobTitle = jobTitle;
        this.overallScore = overallScore;
        this.skillsScore = skillsScore;
        this.experienceScore = experienceScore;
        this.jdRelevanceScore = jdRelevanceScore;
        this.educationScore = educationScore;
        this.matchedSkills = matchedSkills != null ? matchedSkills : new ArrayList<>();
        this.missingSkills = missingSkills != null ? missingSkills : new ArrayList<>();
        this.analysisSummary = analysisSummary;
        this.strengths = strengths != null ? strengths : new ArrayList<>();
        this.weaknesses = weaknesses != null ? weaknesses : new ArrayList<>();
        this.recommendation = recommendation;
    }

    public static AiAnalysisDtoBuilder builder() {
        return new AiAnalysisDtoBuilder();
    }

    public static class AiAnalysisDtoBuilder {
        private Long id;
        private Long applicationId;
        private String candidateName;
        private String candidateEmail;
        private Long resumeId;
        private String resumeFileName;
        private String jobTitle;
        private Integer overallScore;
        private Integer skillsScore;
        private Integer experienceScore;
        private Integer jdRelevanceScore;
        private Integer educationScore;
        private List<String> matchedSkills = new ArrayList<>();
        private List<String> missingSkills = new ArrayList<>();
        private String analysisSummary;
        private List<String> strengths = new ArrayList<>();
        private List<String> weaknesses = new ArrayList<>();
        private RecommendationType recommendation;

        public AiAnalysisDtoBuilder id(Long id) { this.id = id; return this; }
        public AiAnalysisDtoBuilder applicationId(Long applicationId) { this.applicationId = applicationId; return this; }
        public AiAnalysisDtoBuilder candidateName(String candidateName) { this.candidateName = candidateName; return this; }
        public AiAnalysisDtoBuilder candidateEmail(String candidateEmail) { this.candidateEmail = candidateEmail; return this; }
        public AiAnalysisDtoBuilder resumeId(Long resumeId) { this.resumeId = resumeId; return this; }
        public AiAnalysisDtoBuilder resumeFileName(String resumeFileName) { this.resumeFileName = resumeFileName; return this; }
        public AiAnalysisDtoBuilder jobTitle(String jobTitle) { this.jobTitle = jobTitle; return this; }
        public AiAnalysisDtoBuilder overallScore(Integer overallScore) { this.overallScore = overallScore; return this; }
        public AiAnalysisDtoBuilder skillsScore(Integer skillsScore) { this.skillsScore = skillsScore; return this; }
        public AiAnalysisDtoBuilder experienceScore(Integer experienceScore) { this.experienceScore = experienceScore; return this; }
        public AiAnalysisDtoBuilder jdRelevanceScore(Integer jdRelevanceScore) { this.jdRelevanceScore = jdRelevanceScore; return this; }
        public AiAnalysisDtoBuilder educationScore(Integer educationScore) { this.educationScore = educationScore; return this; }
        public AiAnalysisDtoBuilder matchedSkills(List<String> matchedSkills) { this.matchedSkills = matchedSkills; return this; }
        public AiAnalysisDtoBuilder missingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; return this; }
        public AiAnalysisDtoBuilder analysisSummary(String analysisSummary) { this.analysisSummary = analysisSummary; return this; }
        public AiAnalysisDtoBuilder strengths(List<String> strengths) { this.strengths = strengths; return this; }
        public AiAnalysisDtoBuilder weaknesses(List<String> weaknesses) { this.weaknesses = weaknesses; return this; }
        public AiAnalysisDtoBuilder recommendation(RecommendationType recommendation) { this.recommendation = recommendation; return this; }

        public AiAnalysisDto build() {
            return new AiAnalysisDto(id, applicationId, candidateName, candidateEmail, resumeId, resumeFileName, jobTitle, overallScore, skillsScore, experienceScore, jdRelevanceScore, educationScore, matchedSkills, missingSkills, analysisSummary, strengths, weaknesses, recommendation);
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
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public Integer getOverallScore() { return overallScore; }
    public void setOverallScore(Integer overallScore) { this.overallScore = overallScore; }
    public Integer getSkillsScore() { return skillsScore; }
    public void setSkillsScore(Integer skillsScore) { this.skillsScore = skillsScore; }
    public Integer getExperienceScore() { return experienceScore; }
    public void setExperienceScore(Integer experienceScore) { this.experienceScore = experienceScore; }
    public Integer getJdRelevanceScore() { return jdRelevanceScore; }
    public void setJdRelevanceScore(Integer jdRelevanceScore) { this.jdRelevanceScore = jdRelevanceScore; }
    public Integer getEducationScore() { return educationScore; }
    public void setEducationScore(Integer educationScore) { this.educationScore = educationScore; }
    public List<String> getMatchedSkills() { return matchedSkills; }
    public void setMatchedSkills(List<String> matchedSkills) { this.matchedSkills = matchedSkills; }
    public List<String> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; }
    public String getAnalysisSummary() { return analysisSummary; }
    public void setAnalysisSummary(String analysisSummary) { this.analysisSummary = analysisSummary; }
    public List<String> getStrengths() { return strengths; }
    public void setStrengths(List<String> strengths) { this.strengths = strengths; }
    public List<String> getWeaknesses() { return weaknesses; }
    public void setWeaknesses(List<String> weaknesses) { this.weaknesses = weaknesses; }
    public RecommendationType getRecommendation() { return recommendation; }
    public void setRecommendation(RecommendationType recommendation) { this.recommendation = recommendation; }
}
