package com.smarthire.service;

import com.smarthire.entity.Resume;
import com.smarthire.entity.User;
import com.smarthire.repository.ResumeRepository;
import com.smarthire.security.UserPrincipal;
import com.smarthire.service.ai.ResumeParserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ResumeParserService resumeParserService;
    private final UserService userService;

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir = "./uploads";

    public ResumeService(ResumeRepository resumeRepository,
                         ResumeParserService resumeParserService,
                         UserService userService) {
        this.resumeRepository = resumeRepository;
        this.resumeParserService = resumeParserService;
        this.userService = userService;
    }

    @Transactional
    public Resume uploadResume(UserPrincipal principal, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Resume file cannot be empty");
        }

        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "resume.pdf";
        String lowerName = originalFilename.toLowerCase();
        if (!lowerName.endsWith(".pdf") && !lowerName.endsWith(".doc") && !lowerName.endsWith(".docx") && !lowerName.endsWith(".txt")) {
            throw new IllegalArgumentException("Invalid file type. Only PDF, DOC, DOCX, and TXT files are supported.");
        }

        User user = userService.getCurrentUser(principal);

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String storedFilename = UUID.randomUUID() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
        Path targetPath = Paths.get(uploadDir).resolve(storedFilename);

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // Parse resume using Apache Tika
        String extractedText = resumeParserService.extractText(file);
        Double experienceYears = resumeParserService.extractExperienceYears(extractedText);

        // Check if there is already a primary resume
        List<Resume> existingResumes = resumeRepository.findByUserOrderByCreatedAtDesc(user);
        boolean isPrimary = existingResumes.isEmpty();

        Resume resume = Resume.builder()
                .user(user)
                .fileName(originalFilename)
                .filePath(targetPath.toString())
                .contentType(file.getContentType())
                .fileSizeBytes(file.getSize())
                .extractedText(extractedText)
                .parsedExperienceYears(experienceYears)
                .isPrimary(isPrimary)
                .build();

        return resumeRepository.save(resume);
    }

    @Transactional(readOnly = true)
    public List<Resume> getMyResumes(UserPrincipal principal) {
        User user = userService.getCurrentUser(principal);
        return resumeRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional(readOnly = true)
    public Resource getResumeResource(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found"));

        Path path = Paths.get(resume.getFilePath());
        File file = path.toFile();
        if (!file.exists()) {
            throw new IllegalArgumentException("Resume file not found on storage");
        }

        return new FileSystemResource(file);
    }

    @Transactional(readOnly = true)
    public Resume getResumeById(Long resumeId) {
        return resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found"));
    }

    @Transactional
    public void deleteResume(UserPrincipal principal, Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found"));

        if (!resume.getUser().getId().equals(principal.getId())) {
            throw new IllegalArgumentException("Access denied to resume");
        }

        try {
            Path path = Paths.get(resume.getFilePath());
            Files.deleteIfExists(path);
        } catch (Exception ignored) {}

        resumeRepository.delete(resume);
    }

    @Transactional
    public Resume setPrimaryResume(UserPrincipal principal, Long resumeId) {
        User user = userService.getCurrentUser(principal);
        List<Resume> resumes = resumeRepository.findByUserOrderByCreatedAtDesc(user);

        Resume target = null;
        for (Resume r : resumes) {
            if (r.getId().equals(resumeId)) {
                r.setIsPrimary(true);
                target = r;
            } else {
                r.setIsPrimary(false);
            }
        }

        if (target == null) {
            throw new IllegalArgumentException("Resume not found for user");
        }

        resumeRepository.saveAll(resumes);
        return target;
    }
}
