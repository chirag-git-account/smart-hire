package com.smarthire.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthire.dto.AiCandidateFeedbackDto;
import com.smarthire.dto.AiInterviewQuestionsDto;
import com.smarthire.entity.AiAnalysis;
import com.smarthire.entity.Application;
import com.smarthire.entity.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OpenAiService {

    private static final Logger log = LoggerFactory.getLogger(OpenAiService.class);

    @Value("${app.ai.openai.api-key:}")
    private String openAiApiKey;

    @Value("${app.ai.openai.model:gpt-4o-mini}")
    private String openAiModel;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean isConfigured() {
        return openAiApiKey != null && !openAiApiKey.isBlank() && !openAiApiKey.equals("YOUR_OPENAI_API_KEY") && !openAiApiKey.equals("your_openai_api_key_here");
    }

    public AiInterviewQuestionsDto generateInterviewQuestions(Application application, AiAnalysis analysis) {
        if (!isConfigured()) return null;

        Job job = application.getJob();
        String candidateName = application.getCandidate().getFullName();

        String prompt = String.format(
                "You are an expert technical hiring manager for %s conducting interviews for '%s'.\n" +
                "Candidate: %s\n" +
                "Required Skills: %s\n" +
                "Candidate Matched Skills: %s\n" +
                "Candidate Missing Skills: %s\n" +
                "Match Score: %d/100\n\n" +
                "Generate 5 targeted technical, system design, and behavioral interview questions.\n" +
                "Return ONLY a valid JSON object matching:\n" +
                "{\n" +
                "  \"questions\": [\n" +
                "    {\n" +
                "      \"question\": \"Question text\",\n" +
                "      \"category\": \"TECHNICAL\",\n" +
                "      \"difficulty\": \"SENIOR\",\n" +
                "      \"targetSkill\": \"Java\",\n" +
                "      \"expectedAnswerHint\": \"Evaluation criteria and what a good answer includes\"\n" +
                "    }\n" +
                "  ]\n" +
                "}",
                job.getCompanyName(), job.getTitle(), candidateName,
                String.join(", ", job.getRequiredSkills()),
                String.join(", ", analysis.getMatchedSkills()),
                String.join(", ", analysis.getMissingSkills()),
                analysis.getOverallScore()
        );

        try {
            String rawJson = callOpenAiChat(prompt);
            if (rawJson == null) return null;

            JsonNode root = objectMapper.readTree(cleanJsonText(rawJson));
            JsonNode questionsNode = root.has("questions") ? root.get("questions") : root;

            List<AiInterviewQuestionsDto.InterviewQuestionItem> items = new ArrayList<>();
            if (questionsNode.isArray()) {
                for (JsonNode q : questionsNode) {
                    items.add(AiInterviewQuestionsDto.InterviewQuestionItem.builder()
                            .question(q.path("question").asText("Describe your experience with this tech stack."))
                            .category(q.path("category").asText("TECHNICAL"))
                            .difficulty(q.path("difficulty").asText("SENIOR"))
                            .targetSkill(q.path("targetSkill").asText("Core Architecture"))
                            .expectedAnswerHint(q.path("expectedAnswerHint").asText("Clear technical explanation with trade-offs."))
                            .build());
                }
            }

            return AiInterviewQuestionsDto.builder()
                    .candidateName(candidateName)
                    .jobTitle(job.getTitle())
                    .companyName(job.getCompanyName())
                    .generatedBy("OpenAI (" + openAiModel + ")")
                    .questions(items)
                    .build();
        } catch (Exception e) {
            log.warn("OpenAI interview questions generation failed: {}", e.getMessage());
            return null;
        }
    }

    public AiCandidateFeedbackDto generateCandidateFeedback(Application application, AiAnalysis analysis) {
        if (!isConfigured()) return null;

        Job job = application.getJob();
        String candidateName = application.getCandidate().getFullName();

        String prompt = String.format(
                "You are an executive career advisor evaluating %s for '%s' at %s.\n" +
                "Score: %d/100. Matched: %s. Missing: %s.\n" +
                "Return ONLY a JSON object matching:\n" +
                "{\n" +
                "  \"executiveSummary\": \"2-3 sentence evaluation.\",\n" +
                "  \"topStrengths\": [\"Strength 1\", \"Strength 2\"],\n" +
                "  \"resumeImprovementTips\": [\"Actionable bullet point improvement tip\"],\n" +
                "  \"recommendedNextSkills\": [\"Next skill to learn\"],\n" +
                "  \"interviewPrepTip\": \"Key domain topic to prepare before interview.\"\n" +
                "}",
                candidateName, job.getTitle(), job.getCompanyName(),
                analysis.getOverallScore(),
                String.join(", ", analysis.getMatchedSkills()),
                String.join(", ", analysis.getMissingSkills())
        );

        try {
            String rawJson = callOpenAiChat(prompt);
            if (rawJson == null) return null;

            JsonNode root = objectMapper.readTree(cleanJsonText(rawJson));
            List<String> strengths = new ArrayList<>();
            if (root.has("topStrengths") && root.get("topStrengths").isArray()) {
                root.get("topStrengths").forEach(s -> strengths.add(s.asText()));
            }

            List<String> tips = new ArrayList<>();
            if (root.has("resumeImprovementTips") && root.get("resumeImprovementTips").isArray()) {
                root.get("resumeImprovementTips").forEach(s -> tips.add(s.asText()));
            }

            List<String> nextSkills = new ArrayList<>();
            if (root.has("recommendedNextSkills") && root.get("recommendedNextSkills").isArray()) {
                root.get("recommendedNextSkills").forEach(s -> nextSkills.add(s.asText()));
            }

            return AiCandidateFeedbackDto.builder()
                    .candidateName(candidateName)
                    .jobTitle(job.getTitle())
                    .overallScore(analysis.getOverallScore())
                    .generatedBy("OpenAI (" + openAiModel + ")")
                    .executiveSummary(root.path("executiveSummary").asText("Strong technical alignment with competitive competencies."))
                    .topStrengths(!strengths.isEmpty() ? strengths : analysis.getStrengths())
                    .resumeImprovementTips(!tips.isEmpty() ? tips : List.of("Quantify project impact with metrics."))
                    .recommendedNextSkills(!nextSkills.isEmpty() ? nextSkills : analysis.getMissingSkills())
                    .interviewPrepTip(root.path("interviewPrepTip").asText("Review system design trade-offs."))
                    .build();
        } catch (Exception e) {
            log.warn("OpenAI candidate feedback generation failed: {}", e.getMessage());
            return null;
        }
    }

    private String callOpenAiChat(String prompt) {
        try {
            String url = "https://api.openai.com/v1/chat/completions";

            Map<String, Object> message = Map.of("role", "user", "content", prompt);
            Map<String, Object> requestBody = Map.of(
                    "model", openAiModel,
                    "messages", List.of(message),
                    "temperature", 0.3,
                    "response_format", Map.of("type", "json_object")
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey.trim());

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.path("choices").path(0).path("message").path("content").asText();
            }
        } catch (Exception e) {
            log.warn("OpenAI REST API call error: {}", e.getMessage());
        }
        return null;
    }

    private String cleanJsonText(String raw) {
        if (raw == null) return "{}";
        String t = raw.trim();
        if (t.startsWith("```json")) t = t.substring(7);
        else if (t.startsWith("```")) t = t.substring(3);
        if (t.endsWith("```")) t = t.substring(0, t.length() - 3);
        return t.trim();
    }
}
