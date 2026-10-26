package com.smarthire.controller;

import com.smarthire.dto.InterviewScheduleDto;
import com.smarthire.dto.ScheduleInterviewRequest;
import com.smarthire.entity.enums.InterviewStatus;
import com.smarthire.security.UserPrincipal;
import com.smarthire.service.InterviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping("/schedule")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<InterviewScheduleDto> scheduleInterview(@Valid @RequestBody ScheduleInterviewRequest request) {
        return ResponseEntity.ok(interviewService.scheduleInterview(request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<InterviewScheduleDto>> getMyInterviews(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(interviewService.getMyInterviews(principal));
    }

    @GetMapping("/recruiter")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<List<InterviewScheduleDto>> getRecruiterInterviews(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(interviewService.getRecruiterInterviews(principal));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<InterviewScheduleDto> updateStatus(@PathVariable Long id,
                                                             @RequestParam InterviewStatus status,
                                                             @RequestParam(required = false) String rescheduleTime) {
        return ResponseEntity.ok(interviewService.updateStatus(id, status, rescheduleTime));
    }
}
