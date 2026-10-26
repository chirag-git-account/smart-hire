package com.smarthire.controller;

import com.smarthire.entity.Resume;
import com.smarthire.security.UserPrincipal;
import com.smarthire.service.ResumeService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Resume> uploadResume(@AuthenticationPrincipal UserPrincipal principal,
                                               @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(resumeService.uploadResume(principal, file));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Resume>> getMyResumes(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(resumeService.getMyResumes(principal));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long id) {
        Resume resume = resumeService.getResumeById(id);
        Resource resource = resumeService.getResumeResource(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resume.getFileName() + "\"")
                .body(resource);
    }

    @GetMapping("/{id}/view")
    public ResponseEntity<Resource> viewResume(@PathVariable Long id) {
        Resume resume = resumeService.getResumeById(id);
        Resource resource = resumeService.getResumeResource(id);

        MediaType mediaType = MediaType.APPLICATION_PDF;
        if (resume.getContentType() != null && !resume.getContentType().isBlank()) {
            try {
                mediaType = MediaType.parseMediaType(resume.getContentType());
            } catch (Exception ignored) {}
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resume.getFileName() + "\"")
                .body(resource);
    }

    @PatchMapping("/{id}/primary")
    public ResponseEntity<Resume> setPrimaryResume(@AuthenticationPrincipal UserPrincipal principal,
                                                   @PathVariable Long id) {
        return ResponseEntity.ok(resumeService.setPrimaryResume(principal, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteResume(@AuthenticationPrincipal UserPrincipal principal,
                                                           @PathVariable Long id) {
        resumeService.deleteResume(principal, id);
        return ResponseEntity.ok(Map.of("message", "Resume deleted successfully"));
    }
}
