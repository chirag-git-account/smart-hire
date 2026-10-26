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
public class GeminiLlmService {

    private static final Logger log = LoggerFactory.getLogger(GeminiLlmService.class);

    @Value("${app.ai.gemini.api-key:}")
    private String geminiApiKey;

    @Value("${app.ai.gemini.model:gemini-1.5-flash}")
    private String geminiModel;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiInterviewQuestionsDto generateInterviewQuestions(Application application, AiAnalysis analysis) {
        Job job = application.getJob();
        String candidateName = application.getCandidate().getFullName();

        if (geminiApiKey != null && !geminiApiKey.isBlank() && !geminiApiKey.equals("YOUR_GEMINI_API_KEY")) {
            try {
                AiInterviewQuestionsDto geminiResult = callGeminiForQuestions(application, analysis);
                if (geminiResult != null && geminiResult.getQuestions() != null && !geminiResult.getQuestions().isEmpty()) {
                    return geminiResult;
                }
            } catch (Exception e) {
                log.warn("Gemini API call for interview questions failed: {}. Falling back to Smart Engine.", e.getMessage());
            }
        }

        return generateFallbackQuestions(job, candidateName, analysis);
    }

    public AiCandidateFeedbackDto generateCandidateFeedback(Application application, AiAnalysis analysis) {
        Job job = application.getJob();
        String candidateName = application.getCandidate().getFullName();

        if (geminiApiKey != null && !geminiApiKey.isBlank() && !geminiApiKey.equals("YOUR_GEMINI_API_KEY")) {
            try {
                AiCandidateFeedbackDto geminiResult = callGeminiForFeedback(application, analysis);
                if (geminiResult != null) {
                    return geminiResult;
                }
            } catch (Exception e) {
                log.warn("Gemini API call for candidate feedback failed: {}. Falling back to Smart Engine.", e.getMessage());
            }
        }

        return generateFallbackFeedback(job, candidateName, analysis);
    }

    private AiInterviewQuestionsDto callGeminiForQuestions(Application application, AiAnalysis analysis) throws Exception {
        Job job = application.getJob();
        String candidateName = application.getCandidate().getFullName();

        String prompt = String.format(
                "You are an expert technical interviewer for %s hiring for the role of '%s'.\n" +
                "Candidate Name: %s\n" +
                "Required Stack: %s\n" +
                "Candidate Matched Skills: %s\n" +
                "Candidate Missing Skills: %s\n" +
                "Overall Match Score: %d/100\n\n" +
                "Generate 5 precise interview questions tailored to verify this candidate's competencies and probe skill gaps.\n" +
                "Respond ONLY in valid JSON format matching this structure (no markdown fences, just JSON):\n" +
                "{\n" +
                "  \"questions\": [\n" +
                "    {\n" +
                "      \"question\": \"Question text here\",\n" +
                "      \"category\": \"TECHNICAL\",\n" +
                "      \"difficulty\": \"SENIOR\",\n" +
                "      \"targetSkill\": \"Java\",\n" +
                "      \"expectedAnswerHint\": \"What good answers should mention\"\n" +
                "    }\n" +
                "  ]\n" +
                "}",
                job.getCompanyName(), job.getTitle(), candidateName,
                String.join(", ", job.getRequiredSkills()),
                String.join(", ", analysis.getMatchedSkills()),
                String.join(", ", analysis.getMissingSkills()),
                analysis.getOverallScore()
        );

        String jsonResponse = queryGeminiRaw(prompt);
        if (jsonResponse == null) return null;

        // Parse questions from Gemini JSON response
        String cleanJson = extractJsonText(jsonResponse);
        JsonNode root = objectMapper.readTree(cleanJson);
        JsonNode questionsNode = root.has("questions") ? root.get("questions") : root;

        List<AiInterviewQuestionsDto.InterviewQuestionItem> items = new ArrayList<>();
        if (questionsNode.isArray()) {
            for (JsonNode q : questionsNode) {
                items.add(AiInterviewQuestionsDto.InterviewQuestionItem.builder()
                        .question(q.path("question").asText("Describe your experience with distributed systems."))
                        .category(q.path("category").asText("TECHNICAL"))
                        .difficulty(q.path("difficulty").asText("SENIOR"))
                        .targetSkill(q.path("targetSkill").asText("Java / Spring"))
                        .expectedAnswerHint(q.path("expectedAnswerHint").asText("Clear architectural explanation with edge case handling."))
                        .build());
            }
        }

        return AiInterviewQuestionsDto.builder()
                .candidateName(candidateName)
                .jobTitle(job.getTitle())
                .companyName(job.getCompanyName())
                .generatedBy("Google Gemini 1.5 Flash (Live LLM)")
                .questions(items)
                .build();
    }

    private AiCandidateFeedbackDto callGeminiForFeedback(Application application, AiAnalysis analysis) throws Exception {
        Job job = application.getJob();
        String candidateName = application.getCandidate().getFullName();

        String prompt = String.format(
                "You are an executive career coach and technical hiring advisor.\n" +
                "Provide personalized feedback for candidate %s applying for '%s' at %s.\n" +
                "Match Score: %d/100. Matched Skills: %s. Missing Skills: %s.\n" +
                "Return ONLY valid JSON matching this structure:\n" +
                "{\n" +
                "  \"executiveSummary\": \"2-3 sentence personalized evaluation.\",\n" +
                "  \"topStrengths\": [\"Strength 1\", \"Strength 2\"],\n" +
                "  \"resumeImprovementTips\": [\"Actionable tip 1\", \"Actionable tip 2\"],\n" +
                "  \"recommendedNextSkills\": [\"Skill 1\", \"Skill 2\"],\n" +
                "  \"interviewPrepTip\": \"Key topic to review before the technical round.\"\n" +
                "}",
                candidateName, job.getTitle(), job.getCompanyName(),
                analysis.getOverallScore(),
                String.join(", ", analysis.getMatchedSkills()),
                String.join(", ", analysis.getMissingSkills())
        );

        String jsonResponse = queryGeminiRaw(prompt);
        if (jsonResponse == null) return null;

        String cleanJson = extractJsonText(jsonResponse);
        JsonNode root = objectMapper.readTree(cleanJson);

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
                .generatedBy("Google Gemini 1.5 Flash (Live LLM)")
                .executiveSummary(root.path("executiveSummary").asText("Solid candidate profile with competitive baseline competencies."))
                .topStrengths(!strengths.isEmpty() ? strengths : analysis.getStrengths())
                .resumeImprovementTips(!tips.isEmpty() ? tips : List.of("Quantify business metrics with numbers in bullet points."))
                .recommendedNextSkills(!nextSkills.isEmpty() ? nextSkills : analysis.getMissingSkills())
                .interviewPrepTip(root.path("interviewPrepTip").asText("Review distributed caching and transaction isolation levels."))
                .build();
    }

    private String queryGeminiRaw(String promptText) {
        try {
            String url = String.format(
                    "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s",
                    geminiModel, geminiApiKey.trim()
            );

            Map<String, Object> part = Map.of("text", promptText);
            Map<String, Object> content = Map.of("parts", List.of(part));
            Map<String, Object> requestBody = Map.of("contents", List.of(content));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode candidate = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");
                return candidate.asText();
            }
        } catch (Exception e) {
            log.warn("Gemini REST call encountered error: {}", e.getMessage());
        }
        return null;
    }

    private String extractJsonText(String rawText) {
        if (rawText == null) return "{}";
        String trimmed = rawText.trim();
        if (trimmed.startsWith("```json")) {
            trimmed = trimmed.substring(7);
        } else if (trimmed.startsWith("```")) {
            trimmed = trimmed.substring(3);
        }
        if (trimmed.endsWith("```")) {
            trimmed = trimmed.substring(0, trimmed.length() - 3);
        }
        return trimmed.trim();
    }

    private AiInterviewQuestionsDto generateFallbackQuestions(Job job, String candidateName, AiAnalysis analysis) {
        List<AiInterviewQuestionsDto.InterviewQuestionItem> items = new ArrayList<>();
        List<String> matched = analysis.getMatchedSkills() != null ? analysis.getMatchedSkills() : List.of("Java", "Spring Boot");
        List<String> missing = analysis.getMissingSkills() != null ? analysis.getMissingSkills() : Collections.emptyList();

        // Question 1: Core Architecture on top matched skill
        String topSkill = !matched.isEmpty() ? matched.get(0) : "Spring Boot";
        items.add(AiInterviewQuestionsDto.InterviewQuestionItem.builder()
                .question(String.format("How do you architect scalable, high-throughput backend services using %s with zero downtime?", topSkill))
                .category("TECHNICAL")
                .difficulty("SENIOR")
                .targetSkill(topSkill)
                .expectedAnswerHint("Candidate should mention connection pooling, asynchronous worker threads, caching layers (Redis), and stateless session models.")
                .build());

        // Question 2: Database & Transaction Management
        items.add(AiInterviewQuestionsDto.InterviewQuestionItem.builder()
                .question("In high-volume payment processing, how do you prevent race conditions and duplicate transaction processing across distributed nodes?")
                .category("SYSTEM_DESIGN")
                .difficulty("SENIOR")
                .targetSkill("Database / Concurrency")
                .expectedAnswerHint("Expect idempotent API keys, distributed Redis locks (Redlock), optimistic/pessimistic locking, and transactional outbox patterns.")
                .build());

        // Question 3: Probing Missing Skill
        if (!missing.isEmpty()) {
            String probeSkill = missing.get(0);
            items.add(AiInterviewQuestionsDto.InterviewQuestionItem.builder()
                    .question(String.format("Our team heavily leverages %s. What has been your exposure to this technology, or how would you quickly bridge this gap?", probeSkill))
                    .category("SKILL_GAP")
                    .difficulty("MID")
                    .targetSkill(probeSkill)
                    .expectedAnswerHint("Look for fast learning agility, understanding of fundamental abstractions, and hands-on side projects or migration experiences.")
                    .build());
        }

        // Question 4: Microservices / Resilience
        items.add(AiInterviewQuestionsDto.InterviewQuestionItem.builder()
                .question("Explain how you implement Circuit Breakers, Retry policies, and Dead Letter Queues in a microservices pipeline.")
                .category("TECHNICAL")
                .difficulty("SENIOR")
                .targetSkill("Microservices & Kafka")
                .expectedAnswerHint("Should reference Resilience4j, Kafka consumer offset handling, and exponential backoff retry algorithms.")
                .build());

        // Question 5: Behavioral / Cultural Alignment
        items.add(AiInterviewQuestionsDto.InterviewQuestionItem.builder()
                .question(String.format("Tell us about a production incident you diagnosed at scale and how you communicated RCA to cross-functional stakeholders at %s.", job.getCompanyName()))
                .category("BEHAVIORAL")
                .difficulty("MID")
                .targetSkill("Problem Solving & RCA")
                .expectedAnswerHint("Look for blameless post-mortem methodology, clear observability tracing with Prometheus/Grafana, and preventive safeguards.")
                .build());

        return AiInterviewQuestionsDto.builder()
                .candidateName(candidateName)
                .jobTitle(job.getTitle())
                .companyName(job.getCompanyName())
                .generatedBy("Smart Hire NLP AI Engine (In-House)")
                .questions(items)
                .build();
    }

    private AiCandidateFeedbackDto generateFallbackFeedback(Job job, String candidateName, AiAnalysis analysis) {
        List<String> tips = new ArrayList<>();
        tips.add("Add quantifiable production metrics to bullet points (e.g. 'Reduced latency by 45%', 'Scaled throughput to 10k req/s').");
        tips.add("Highlight architecture diagrams and end-to-end cloud deployment experience on AWS / Docker.");

        List<String> nextSkills = new ArrayList<>();
        if (analysis.getMissingSkills() != null && !analysis.getMissingSkills().isEmpty()) {
            nextSkills.addAll(analysis.getMissingSkills());
        } else {
            nextSkills.add("Distributed Tracing (OpenTelemetry)");
            nextSkills.add("Kafka Event Streaming");
        }

        return AiCandidateFeedbackDto.builder()
                .candidateName(candidateName)
                .jobTitle(job.getTitle())
                .overallScore(analysis.getOverallScore())
                .generatedBy("Smart Hire NLP AI Engine (In-House)")
                .executiveSummary(String.format(
                        "Candidate demonstrates %d%% compatibility for '%s' at %s with strong baseline competencies in %s.",
                        analysis.getOverallScore(), job.getTitle(), job.getCompanyName(),
                        String.join(", ", analysis.getMatchedSkills())
                ))
                .topStrengths(analysis.getStrengths())
                .resumeImprovementTips(tips)
                .recommendedNextSkills(nextSkills)
                .interviewPrepTip("Review system design trade-offs between ACID consistency vs Eventual Consistency for " + job.getCompanyName() + "'s domain.")
                .build();
    }
}
