package com.smarthire.controller;

import com.smarthire.dto.JobDto;
import com.smarthire.security.UserPrincipal;
import com.smarthire.service.SavedJobService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/saved-jobs")
public class SavedJobController {

    private final SavedJobService savedJobService;

    public SavedJobController(SavedJobService savedJobService) {
        this.savedJobService = savedJobService;
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<Void> saveJob(@AuthenticationPrincipal UserPrincipal principal,
                                        @PathVariable Long jobId) {
        savedJobService.saveJob(principal, jobId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<JobDto>> getSavedJobs(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(savedJobService.getSavedJobs(principal));
    }
}
