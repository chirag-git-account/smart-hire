package com.smarthire.dto;

import java.util.List;

public class AiInterviewQuestionsDto {
    private String candidateName;
    private String jobTitle;
    private String companyName;
    private String generatedBy;
    private List<InterviewQuestionItem> questions;

    public AiInterviewQuestionsDto() {}

    public AiInterviewQuestionsDto(String candidateName, String jobTitle, String companyName, String generatedBy, List<InterviewQuestionItem> questions) {
        this.candidateName = candidateName;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.generatedBy = generatedBy;
        this.questions = questions;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String candidateName;
        private String jobTitle;
        private String companyName;
        private String generatedBy;
        private List<InterviewQuestionItem> questions;

        public Builder candidateName(String candidateName) { this.candidateName = candidateName; return this; }
        public Builder jobTitle(String jobTitle) { this.jobTitle = jobTitle; return this; }
        public Builder companyName(String companyName) { this.companyName = companyName; return this; }
        public Builder generatedBy(String generatedBy) { this.generatedBy = generatedBy; return this; }
        public Builder questions(List<InterviewQuestionItem> questions) { this.questions = questions; return this; }

        public AiInterviewQuestionsDto build() {
            return new AiInterviewQuestionsDto(candidateName, jobTitle, companyName, generatedBy, questions);
        }
    }

    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }
    public List<InterviewQuestionItem> getQuestions() { return questions; }
    public void setQuestions(List<InterviewQuestionItem> questions) { this.questions = questions; }

    public static class InterviewQuestionItem {
        private String question;
        private String category;
        private String difficulty;
        private String targetSkill;
        private String expectedAnswerHint;

        public InterviewQuestionItem() {}

        public InterviewQuestionItem(String question, String category, String difficulty, String targetSkill, String expectedAnswerHint) {
            this.question = question;
            this.category = category;
            this.difficulty = difficulty;
            this.targetSkill = targetSkill;
            this.expectedAnswerHint = expectedAnswerHint;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String question;
            private String category;
            private String difficulty;
            private String targetSkill;
            private String expectedAnswerHint;

            public Builder question(String question) { this.question = question; return this; }
            public Builder category(String category) { this.category = category; return this; }
            public Builder difficulty(String difficulty) { this.difficulty = difficulty; return this; }
            public Builder targetSkill(String targetSkill) { this.targetSkill = targetSkill; return this; }
            public Builder expectedAnswerHint(String expectedAnswerHint) { this.expectedAnswerHint = expectedAnswerHint; return this; }

            public InterviewQuestionItem build() {
                return new InterviewQuestionItem(question, category, difficulty, targetSkill, expectedAnswerHint);
            }
        }

        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getDifficulty() { return difficulty; }
        public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
        public String getTargetSkill() { return targetSkill; }
        public void setTargetSkill(String targetSkill) { this.targetSkill = targetSkill; }
        public String getExpectedAnswerHint() { return expectedAnswerHint; }
        public void setExpectedAnswerHint(String expectedAnswerHint) { this.expectedAnswerHint = expectedAnswerHint; }
    }
}
