package com.smarthire.service.ai;

import com.smarthire.dto.AiCandidateFeedbackDto;
import com.smarthire.dto.AiInterviewQuestionsDto;
import com.smarthire.entity.AiAnalysis;
import com.smarthire.entity.Application;
import org.springframework.stereotype.Service;

@Service
public class AiServiceRouter {

    private final OpenAiService openAiService;
    private final GeminiLlmService geminiLlmService;

    public AiServiceRouter(OpenAiService openAiService, GeminiLlmService geminiLlmService) {
        this.openAiService = openAiService;
        this.geminiLlmService = geminiLlmService;
    }

    public AiInterviewQuestionsDto generateInterviewQuestions(Application application, AiAnalysis analysis) {
        // Priority 1: OpenAI
        if (openAiService.isConfigured()) {
            AiInterviewQuestionsDto openAiResult = openAiService.generateInterviewQuestions(application, analysis);
            if (openAiResult != null && openAiResult.getQuestions() != null && !openAiResult.getQuestions().isEmpty()) {
                return openAiResult;
            }
        }

        // Priority 2: Gemini / Fallback
        return geminiLlmService.generateInterviewQuestions(application, analysis);
    }

    public AiCandidateFeedbackDto generateCandidateFeedback(Application application, AiAnalysis analysis) {
        // Priority 1: OpenAI
        if (openAiService.isConfigured()) {
            AiCandidateFeedbackDto openAiResult = openAiService.generateCandidateFeedback(application, analysis);
            if (openAiResult != null) {
                return openAiResult;
            }
        }

        // Priority 2: Gemini / Fallback
        return geminiLlmService.generateCandidateFeedback(application, analysis);
    }
}
