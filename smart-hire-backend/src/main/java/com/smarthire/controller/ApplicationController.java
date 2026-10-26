package com.smarthire.controller;

import com.smarthire.dto.ApplicationDto;
import com.smarthire.dto.ApplyJobRequest;
import com.smarthire.security.UserPrincipal;
import com.smarthire.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<ApplicationDto> applyForJob(@AuthenticationPrincipal UserPrincipal principal,
                                                      @Valid @RequestBody ApplyJobRequest request) {
        return ResponseEntity.ok(applicationService.applyForJob(principal, request));
    }

    @PostMapping("/apply")
    public ResponseEntity<ApplicationDto> applyForJobAlias(@AuthenticationPrincipal UserPrincipal principal,
                                                           @Valid @RequestBody ApplyJobRequest request) {
        return ResponseEntity.ok(applicationService.applyForJob(principal, request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ApplicationDto>> getMyApplications(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(applicationService.getMyApplications(principal));
    }
}
