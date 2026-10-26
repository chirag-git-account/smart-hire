package com.smarthire.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smarthire.entity.enums.RecommendationType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ai_analyses")
public class AiAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    @JsonIgnore
    private Application application;

    private Integer overallScore = 0;
    private Integer skillsScore = 0;
    private Integer experienceScore = 0;
    private Integer jdRelevanceScore = 0;
    private Integer educationScore = 0;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ai_matched_skills", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "skill_name")
    private List<String> matchedSkills = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ai_missing_skills", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "skill_name")
    private List<String> missingSkills = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String analysisSummary;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ai_strengths", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "strength_text")
    private List<String> strengths = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ai_weaknesses", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "weakness_text")
    private List<String> weaknesses = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecommendationType recommendation = RecommendationType.MODERATE_MATCH;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public AiAnalysis() {}

    public AiAnalysis(Long id, Application application, Integer overallScore, Integer skillsScore, Integer experienceScore, Integer jdRelevanceScore, Integer educationScore, List<String> matchedSkills, List<String> missingSkills, String analysisSummary, List<String> strengths, List<String> weaknesses, RecommendationType recommendation, LocalDateTime createdAt) {
        this.id = id;
        this.application = application;
        this.overallScore = overallScore != null ? overallScore : 0;
        this.skillsScore = skillsScore != null ? skillsScore : 0;
        this.experienceScore = experienceScore != null ? experienceScore : 0;
        this.jdRelevanceScore = jdRelevanceScore != null ? jdRelevanceScore : 0;
        this.educationScore = educationScore != null ? educationScore : 0;
        this.matchedSkills = matchedSkills != null ? matchedSkills : new ArrayList<>();
        this.missingSkills = missingSkills != null ? missingSkills : new ArrayList<>();
        this.analysisSummary = analysisSummary;
        this.strengths = strengths != null ? strengths : new ArrayList<>();
        this.weaknesses = weaknesses != null ? weaknesses : new ArrayList<>();
        this.recommendation = recommendation != null ? recommendation : RecommendationType.MODERATE_MATCH;
        this.createdAt = createdAt;
    }

    public static AiAnalysisBuilder builder() {
        return new AiAnalysisBuilder();
    }

    public static class AiAnalysisBuilder {
        private Long id;
        private Application application;
        private Integer overallScore = 0;
        private Integer skillsScore = 0;
        private Integer experienceScore = 0;
        private Integer jdRelevanceScore = 0;
        private Integer educationScore = 0;
        private List<String> matchedSkills = new ArrayList<>();
        private List<String> missingSkills = new ArrayList<>();
        private String analysisSummary;
        private List<String> strengths = new ArrayList<>();
        private List<String> weaknesses = new ArrayList<>();
        private RecommendationType recommendation = RecommendationType.MODERATE_MATCH;
        private LocalDateTime createdAt;

        public AiAnalysisBuilder id(Long id) { this.id = id; return this; }
        public AiAnalysisBuilder application(Application application) { this.application = application; return this; }
        public AiAnalysisBuilder overallScore(Integer overallScore) { this.overallScore = overallScore; return this; }
        public AiAnalysisBuilder skillsScore(Integer skillsScore) { this.skillsScore = skillsScore; return this; }
        public AiAnalysisBuilder experienceScore(Integer experienceScore) { this.experienceScore = experienceScore; return this; }
        public AiAnalysisBuilder jdRelevanceScore(Integer jdRelevanceScore) { this.jdRelevanceScore = jdRelevanceScore; return this; }
        public AiAnalysisBuilder educationScore(Integer educationScore) { this.educationScore = educationScore; return this; }
        public AiAnalysisBuilder matchedSkills(List<String> matchedSkills) { this.matchedSkills = matchedSkills; return this; }
        public AiAnalysisBuilder missingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; return this; }
        public AiAnalysisBuilder analysisSummary(String analysisSummary) { this.analysisSummary = analysisSummary; return this; }
        public AiAnalysisBuilder strengths(List<String> strengths) { this.strengths = strengths; return this; }
        public AiAnalysisBuilder weaknesses(List<String> weaknesses) { this.weaknesses = weaknesses; return this; }
        public AiAnalysisBuilder recommendation(RecommendationType recommendation) { this.recommendation = recommendation; return this; }
        public AiAnalysisBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AiAnalysis build() {
            return new AiAnalysis(id, application, overallScore, skillsScore, experienceScore, jdRelevanceScore, educationScore, matchedSkills, missingSkills, analysisSummary, strengths, weaknesses, recommendation, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Application getApplication() { return application; }
    public void setApplication(Application application) { this.application = application; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
