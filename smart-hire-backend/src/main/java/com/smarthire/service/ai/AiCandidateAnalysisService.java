package com.smarthire.service.ai;

import com.smarthire.entity.AiAnalysis;
import com.smarthire.entity.Application;
import com.smarthire.entity.Job;
import com.smarthire.entity.Resume;
import com.smarthire.entity.UserProfile;
import com.smarthire.entity.enums.RecommendationType;
import com.smarthire.repository.AiAnalysisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AiCandidateAnalysisService {

    private final AiAnalysisRepository aiAnalysisRepository;

    public AiCandidateAnalysisService(AiAnalysisRepository aiAnalysisRepository) {
        this.aiAnalysisRepository = aiAnalysisRepository;
    }

    @Transactional
    public AiAnalysis generateOrUpdateAnalysis(Application application) {
        Job job = application.getJob();
        Resume resume = application.getResume();
        UserProfile profile = application.getCandidate().getProfile();

        String resumeText = resume != null && resume.getExtractedText() != null 
                ? resume.getExtractedText().toLowerCase() 
                : "";

        List<String> userSkills = new ArrayList<>();
        if (profile != null && profile.getSkills() != null) {
            userSkills.addAll(profile.getSkills());
        }

        // 1. Skill Match Score (35%)
        List<String> requiredSkills = job.getRequiredSkills() != null ? job.getRequiredSkills() : Collections.emptyList();
        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (String reqSkill : requiredSkills) {
            String cleanSkill = reqSkill.trim().toLowerCase();
            boolean inResume = resumeText.contains(cleanSkill);
            boolean inProfile = userSkills.stream().anyMatch(s -> s.trim().equalsIgnoreCase(cleanSkill));

            if (inResume || inProfile) {
                matchedSkills.add(reqSkill.trim());
            } else {
                missingSkills.add(reqSkill.trim());
            }
        }

        int skillsScore = requiredSkills.isEmpty() ? 85 : (int) Math.round(((double) matchedSkills.size() / requiredSkills.size()) * 100);

        // 2. Experience Score (25%)
        double candidateExp = resume != null && resume.getParsedExperienceYears() != null 
                ? resume.getParsedExperienceYears() 
                : (profile != null && profile.getTotalExperienceYears() != null ? profile.getTotalExperienceYears() : 0.0);
        int requiredExp = job.getExperienceMinYears() != null ? job.getExperienceMinYears() : 0;

        int experienceScore;
        if (requiredExp == 0) {
            experienceScore = 90;
        } else if (candidateExp >= requiredExp) {
            experienceScore = Math.min(100, 85 + (int) ((candidateExp - requiredExp) * 5));
        } else {
            experienceScore = Math.max(30, (int) Math.round((candidateExp / requiredExp) * 80));
        }

        // 3. JD Relevance Score (20%)
        String jdText = (job.getTitle() + " " + job.getDescription() + " " + (job.getResponsibilities() != null ? job.getResponsibilities() : "")).toLowerCase();
        Set<String> jdKeywords = Arrays.stream(jdText.split("\\W+"))
                .filter(w -> w.length() > 3)
                .collect(Collectors.toSet());

        long keywordHits = jdKeywords.stream().filter(resumeText::contains).count();
        int jdRelevanceScore = jdKeywords.isEmpty() ? 80 : Math.min(95, Math.max(40, (int) ((double) keywordHits / Math.min(jdKeywords.size(), 20) * 100)));

        // 4. Education Score (20%)
        int educationScore = 80;
        if (resumeText.contains("b.tech") || resumeText.contains("b.e.") || resumeText.contains("m.tech") || resumeText.contains("bachelor") || resumeText.contains("master") || resumeText.contains("computer science") || resumeText.contains("mca")) {
            educationScore = 95;
        }

        // Overall Weighted Score
        int overallScore = (int) Math.round(
                (skillsScore * 0.35) + 
                (experienceScore * 0.25) + 
                (jdRelevanceScore * 0.20) + 
                (educationScore * 0.20)
        );

        // Recommendation
        RecommendationType recommendation;
        if (overallScore >= 80) {
            recommendation = RecommendationType.STRONG_MATCH;
        } else if (overallScore >= 65) {
            recommendation = RecommendationType.GOOD_MATCH;
        } else if (overallScore >= 50) {
            recommendation = RecommendationType.MODERATE_MATCH;
        } else {
            recommendation = RecommendationType.LOW_MATCH;
        }

        // Strengths & Weaknesses
        List<String> strengths = new ArrayList<>();
        List<String> weaknesses = new ArrayList<>();

        if (skillsScore >= 75) {
            strengths.add("Strong technical alignment with required core stack (" + matchedSkills.size() + " key skills matched).");
        } else {
            weaknesses.add("Key missing technical competencies: " + String.join(", ", missingSkills));
        }

        if (candidateExp >= requiredExp) {
            strengths.add(String.format("Meets required experience threshold (%.1f years vs %d years required).", candidateExp, requiredExp));
        } else {
            weaknesses.add(String.format("Experience (%.1f years) is below target minimum of %d years.", candidateExp, requiredExp));
        }

        if (jdRelevanceScore >= 70) {
            strengths.add("Resume exhibits high domain familiarity and relevant project terminology.");
        }

        if (strengths.isEmpty()) strengths.add("Foundational technical readiness for role entry.");
        if (weaknesses.isEmpty()) weaknesses.add("None identified; candidate is well aligned across criteria.");

        String candidateName = application.getCandidate().getFullName();
        String summary = String.format(
                "AI Assessment for %s: Overall %d%% match for '%s' at %s. Demonstrates %d%% skill compatibility with %d matched technologies. Recommended Action: %s.",
                candidateName,
                overallScore,
                job.getTitle(),
                job.getCompanyName(),
                skillsScore,
                matchedSkills.size(),
                recommendation.name().replace('_', ' ')
        );

        AiAnalysis analysis = aiAnalysisRepository.findByApplication(application)
                .orElse(AiAnalysis.builder().application(application).build());

        analysis.setOverallScore(overallScore);
        analysis.setSkillsScore(skillsScore);
        analysis.setExperienceScore(experienceScore);
        analysis.setJdRelevanceScore(jdRelevanceScore);
        analysis.setEducationScore(educationScore);
        analysis.setMatchedSkills(matchedSkills);
        analysis.setMissingSkills(missingSkills);
        analysis.setAnalysisSummary(summary);
        analysis.setStrengths(strengths);
        analysis.setWeaknesses(weaknesses);
        analysis.setRecommendation(recommendation);

        return aiAnalysisRepository.save(analysis);
    }
}
