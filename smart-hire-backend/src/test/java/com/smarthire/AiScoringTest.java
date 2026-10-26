package com.smarthire;

import com.smarthire.entity.*;
import com.smarthire.entity.enums.RecommendationType;
import com.smarthire.repository.AiAnalysisRepository;
import com.smarthire.service.ai.AiCandidateAnalysisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiScoringTest {

    @Mock
    private AiAnalysisRepository aiAnalysisRepository;

    @InjectMocks
    private AiCandidateAnalysisService aiCandidateAnalysisService;

    @Test
    void testAiScoringAlgorithm_StrongMatch() {
        Job job = Job.builder()
                .id(1L)
                .title("Senior Java Engineer")
                .description("Build high-scale distributed backend systems using Java and Spring Boot")
                .requiredSkills(Arrays.asList("Java", "Spring Boot", "MySQL", "Docker"))
                .experienceMinYears(3)
                .companyName("Razorpay")
                .build();

        User candidate = User.builder()
                .id(2L)
                .fullName("Rahul Sharma")
                .email("rahul@example.com")
                .build();

        Resume resume = Resume.builder()
                .id(3L)
                .extractedText("Senior software developer with 5+ years building backend microservices in Java, Spring Boot, MySQL, Docker, and AWS. B.Tech Computer Science graduate.")
                .parsedExperienceYears(5.0)
                .build();

        Application application = Application.builder()
                .id(4L)
                .job(job)
                .candidate(candidate)
                .resume(resume)
                .build();

        when(aiAnalysisRepository.findByApplication(application)).thenReturn(Optional.empty());
        when(aiAnalysisRepository.save(any(AiAnalysis.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AiAnalysis analysis = aiCandidateAnalysisService.generateOrUpdateAnalysis(application);

        assertNotNull(analysis);
        assertTrue(analysis.getOverallScore() >= 80, "Overall score should be >= 80 for strong candidate");
        assertEquals(RecommendationType.STRONG_MATCH, analysis.getRecommendation());
        assertEquals(4, analysis.getMatchedSkills().size());
        assertTrue(analysis.getMissingSkills().isEmpty());
    }

    @Test
    void testAiScoringAlgorithm_SkillGapDetection() {
        Job job = Job.builder()
                .id(1L)
                .title("Machine Learning Specialist")
                .description("Train deep learning models using PyTorch, TensorFlow, Python, and CUDA")
                .requiredSkills(Arrays.asList("Python", "PyTorch", "TensorFlow", "CUDA", "Kubernetes"))
                .experienceMinYears(4)
                .companyName("Google")
                .build();

        User candidate = User.builder().id(2L).fullName("Junior Dev").build();

        Resume resume = Resume.builder()
                .id(3L)
                .extractedText("Junior frontend developer with 1 year experience in HTML, CSS, JavaScript, and React.")
                .parsedExperienceYears(1.0)
                .build();

        Application application = Application.builder()
                .id(4L)
                .job(job)
                .candidate(candidate)
                .resume(resume)
                .build();

        when(aiAnalysisRepository.findByApplication(application)).thenReturn(Optional.empty());
        when(aiAnalysisRepository.save(any(AiAnalysis.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AiAnalysis analysis = aiCandidateAnalysisService.generateOrUpdateAnalysis(application);

        assertNotNull(analysis);
        assertTrue(analysis.getOverallScore() < 60, "Overall score should reflect junior skill mismatch");
        assertTrue(analysis.getMissingSkills().contains("PyTorch"));
        assertTrue(analysis.getMissingSkills().contains("CUDA"));
    }
}
