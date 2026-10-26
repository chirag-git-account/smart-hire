package com.smarthire.controller;

import com.smarthire.dto.AiAnalysisDto;
import com.smarthire.dto.AiCandidateFeedbackDto;
import com.smarthire.dto.AiInterviewQuestionsDto;
import com.smarthire.entity.AiAnalysis;
import com.smarthire.entity.Application;
import com.smarthire.repository.AiAnalysisRepository;
import com.smarthire.repository.ApplicationRepository;
import com.smarthire.service.ai.AiCandidateAnalysisService;
import com.smarthire.service.ai.AiServiceRouter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AiAnalysisController {

    private final AiAnalysisRepository aiAnalysisRepository;
    private final ApplicationRepository applicationRepository;
    private final AiCandidateAnalysisService aiAnalysisService;
    private final AiServiceRouter aiServiceRouter;

    public AiAnalysisController(AiAnalysisRepository aiAnalysisRepository,
                                ApplicationRepository applicationRepository,
                                AiCandidateAnalysisService aiAnalysisService,
                                AiServiceRouter aiServiceRouter) {
        this.aiAnalysisRepository = aiAnalysisRepository;
        this.applicationRepository = applicationRepository;
        this.aiAnalysisService = aiAnalysisService;
        this.aiServiceRouter = aiServiceRouter;
    }

    @GetMapping("/analysis/{applicationId}")
    public ResponseEntity<AiAnalysisDto> getAnalysis(@PathVariable Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        AiAnalysis analysis = aiAnalysisRepository.findByApplication(app)
                .orElseGet(() -> aiAnalysisService.generateOrUpdateAnalysis(app));

        return ResponseEntity.ok(mapToDto(analysis, app));
    }

    @PostMapping("/analysis/{applicationId}/rescore")
    public ResponseEntity<AiAnalysisDto> rescore(@PathVariable Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        AiAnalysis analysis = aiAnalysisService.generateOrUpdateAnalysis(app);
        return ResponseEntity.ok(mapToDto(analysis, app));
    }

    @GetMapping("/analysis/{applicationId}/interview-questions")
    public ResponseEntity<AiInterviewQuestionsDto> getInterviewQuestions(@PathVariable Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        AiAnalysis analysis = aiAnalysisRepository.findByApplication(app)
                .orElseGet(() -> aiAnalysisService.generateOrUpdateAnalysis(app));

        return ResponseEntity.ok(aiServiceRouter.generateInterviewQuestions(app, analysis));
    }

    @GetMapping("/analysis/{applicationId}/candidate-feedback")
    public ResponseEntity<AiCandidateFeedbackDto> getCandidateFeedback(@PathVariable Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        AiAnalysis analysis = aiAnalysisRepository.findByApplication(app)
                .orElseGet(() -> aiAnalysisService.generateOrUpdateAnalysis(app));

        return ResponseEntity.ok(aiServiceRouter.generateCandidateFeedback(app, analysis));
    }

    private AiAnalysisDto mapToDto(AiAnalysis ai, Application app) {
        return AiAnalysisDto.builder()
                .id(ai.getId())
                .applicationId(app.getId())
                .candidateName(app.getCandidate().getFullName())
                .candidateEmail(app.getCandidate().getEmail())
                .resumeId(app.getResume() != null ? app.getResume().getId() : null)
                .resumeFileName(app.getResume() != null ? app.getResume().getFileName() : "Resume.pdf")
                .jobTitle(app.getJob().getTitle())
                .overallScore(ai.getOverallScore())
                .skillsScore(ai.getSkillsScore())
                .experienceScore(ai.getExperienceScore())
                .jdRelevanceScore(ai.getJdRelevanceScore())
                .educationScore(ai.getEducationScore())
                .matchedSkills(ai.getMatchedSkills())
                .missingSkills(ai.getMissingSkills())
                .analysisSummary(ai.getAnalysisSummary())
                .strengths(ai.getStrengths())
                .weaknesses(ai.getWeaknesses())
                .recommendation(ai.getRecommendation())
                .build();
    }
}
