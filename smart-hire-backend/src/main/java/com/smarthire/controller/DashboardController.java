package com.smarthire.controller;

import com.smarthire.dto.RecruiterStatsDto;
import com.smarthire.dto.SeekerStatsDto;
import com.smarthire.security.UserPrincipal;
import com.smarthire.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/seeker-stats")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<SeekerStatsDto> getSeekerStats(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(dashboardService.getSeekerStats(principal));
    }

    @GetMapping("/recruiter-stats")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<RecruiterStatsDto> getRecruiterStats(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(dashboardService.getRecruiterStats(principal));
    }
}
