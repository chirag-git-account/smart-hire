package com.smarthire;

import com.smarthire.service.ai.ResumeParserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmartHireApplicationTests {

    private final ResumeParserService resumeParserService = new ResumeParserService();

    @Test
    void testExtractExperienceYears() {
        String resumeText = "Senior Java Engineer with 5+ years of experience building Spring Boot APIs and MySQL databases.";
        Double exp = resumeParserService.extractExperienceYears(resumeText);
        assertEquals(5.0, exp);
    }

    @Test
    void testExtractExperienceYearsWithDecimal() {
        String resumeText = "Full Stack developer with 3.5 yrs experience in React and Java.";
        Double exp = resumeParserService.extractExperienceYears(resumeText);
        assertEquals(3.5, exp);
    }
}
