package com.smarthire.service.ai;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ResumeParserService {

    private static final Logger log = LoggerFactory.getLogger(ResumeParserService.class);
    private final Tika tika = new Tika();

    public String extractText(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String text = tika.parseToString(inputStream);
            return text != null ? text.replaceAll("\\s+", " ").trim() : "";
        } catch (Exception e) {
            log.error("Failed to parse resume with Apache Tika: {}", e.getMessage());
            return "";
        }
    }

    public Double extractExperienceYears(String resumeText) {
        if (resumeText == null || resumeText.isBlank()) return 0.0;

        Pattern pattern = Pattern.compile("(?i)(\\d+(?:\\.\\d+)?)\\s*\\+?\\s*(?:years|yrs|year|yoe)\\b");
        Matcher matcher = pattern.matcher(resumeText);
        double maxExp = 0.0;

        while (matcher.find()) {
            try {
                double exp = Double.parseDouble(matcher.group(1));
                if (exp > 0 && exp <= 40) {
                    if (exp > maxExp) maxExp = exp;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        return maxExp > 0 ? maxExp : 2.0; // Sensible fallback
    }
}
