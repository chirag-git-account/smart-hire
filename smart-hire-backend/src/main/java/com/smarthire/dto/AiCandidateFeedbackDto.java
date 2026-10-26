package com.smarthire.dto;

import java.util.List;

public class AiCandidateFeedbackDto {
    private String candidateName;
    private String jobTitle;
    private Integer overallScore;
    private String generatedBy;
    private String executiveSummary;
    private List<String> topStrengths;
    private List<String> resumeImprovementTips;
    private List<String> recommendedNextSkills;
    private String interviewPrepTip;

    public AiCandidateFeedbackDto() {}

    public AiCandidateFeedbackDto(String candidateName, String jobTitle, Integer overallScore, String generatedBy,
                                  String executiveSummary, List<String> topStrengths, List<String> resumeImprovementTips,
                                  List<String> recommendedNextSkills, String interviewPrepTip) {
        this.candidateName = candidateName;
        this.jobTitle = jobTitle;
        this.overallScore = overallScore;
        this.generatedBy = generatedBy;
        this.executiveSummary = executiveSummary;
        this.topStrengths = topStrengths;
        this.resumeImprovementTips = resumeImprovementTips;
        this.recommendedNextSkills = recommendedNextSkills;
        this.interviewPrepTip = interviewPrepTip;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String candidateName;
        private String jobTitle;
        private Integer overallScore;
        private String generatedBy;
        private String executiveSummary;
        private List<String> topStrengths;
        private List<String> resumeImprovementTips;
        private List<String> recommendedNextSkills;
        private String interviewPrepTip;

        public Builder candidateName(String candidateName) { this.candidateName = candidateName; return this; }
        public Builder jobTitle(String jobTitle) { this.jobTitle = jobTitle; return this; }
        public Builder overallScore(Integer overallScore) { this.overallScore = overallScore; return this; }
        public Builder generatedBy(String generatedBy) { this.generatedBy = generatedBy; return this; }
        public Builder executiveSummary(String executiveSummary) { this.executiveSummary = executiveSummary; return this; }
        public Builder topStrengths(List<String> topStrengths) { this.topStrengths = topStrengths; return this; }
        public Builder resumeImprovementTips(List<String> resumeImprovementTips) { this.resumeImprovementTips = resumeImprovementTips; return this; }
        public Builder recommendedNextSkills(List<String> recommendedNextSkills) { this.recommendedNextSkills = recommendedNextSkills; return this; }
        public Builder interviewPrepTip(String interviewPrepTip) { this.interviewPrepTip = interviewPrepTip; return this; }

        public AiCandidateFeedbackDto build() {
            return new AiCandidateFeedbackDto(candidateName, jobTitle, overallScore, generatedBy, executiveSummary,
                    topStrengths, resumeImprovementTips, recommendedNextSkills, interviewPrepTip);
        }
    }

    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public Integer getOverallScore() { return overallScore; }
    public void setOverallScore(Integer overallScore) { this.overallScore = overallScore; }
    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }
    public String getExecutiveSummary() { return executiveSummary; }
    public void setExecutiveSummary(String executiveSummary) { this.executiveSummary = executiveSummary; }
    public List<String> getTopStrengths() { return topStrengths; }
    public void setTopStrengths(List<String> topStrengths) { this.topStrengths = topStrengths; }
    public List<String> getResumeImprovementTips() { return resumeImprovementTips; }
    public void setResumeImprovementTips(List<String> resumeImprovementTips) { this.resumeImprovementTips = resumeImprovementTips; }
    public List<String> getRecommendedNextSkills() { return recommendedNextSkills; }
    public void setRecommendedNextSkills(List<String> recommendedNextSkills) { this.recommendedNextSkills = recommendedNextSkills; }
    public String getInterviewPrepTip() { return interviewPrepTip; }
    public void setInterviewPrepTip(String interviewPrepTip) { this.interviewPrepTip = interviewPrepTip; }
}
