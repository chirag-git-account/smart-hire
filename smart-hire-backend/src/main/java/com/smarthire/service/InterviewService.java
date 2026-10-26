package com.smarthire.service;

import com.smarthire.dto.InterviewScheduleDto;
import com.smarthire.dto.ScheduleInterviewRequest;
import com.smarthire.entity.Application;
import com.smarthire.entity.InterviewSchedule;
import com.smarthire.entity.User;
import com.smarthire.entity.enums.ApplicationStatus;
import com.smarthire.entity.enums.InterviewStatus;
import com.smarthire.entity.enums.NotificationType;
import com.smarthire.repository.ApplicationRepository;
import com.smarthire.repository.InterviewScheduleRepository;
import com.smarthire.repository.UserRepository;
import com.smarthire.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterviewService {

    private final InterviewScheduleRepository interviewScheduleRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public InterviewService(InterviewScheduleRepository interviewScheduleRepository,
                            ApplicationRepository applicationRepository,
                            UserRepository userRepository,
                            NotificationService notificationService) {
        this.interviewScheduleRepository = interviewScheduleRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public InterviewScheduleDto scheduleInterview(ScheduleInterviewRequest request) {
        Application app = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        LocalDateTime scheduledTime;
        try {
            scheduledTime = LocalDateTime.parse(request.getScheduledAt());
        } catch (Exception e) {
            try {
                scheduledTime = LocalDateTime.parse(request.getScheduledAt(), DateTimeFormatter.ISO_DATE_TIME);
            } catch (Exception ex) {
                scheduledTime = LocalDateTime.now().plusDays(2);
            }
        }

        InterviewSchedule schedule = interviewScheduleRepository.findByApplication(app)
                .orElse(InterviewSchedule.builder().application(app).build());

        schedule.setScheduledAt(scheduledTime);
        schedule.setDurationMinutes(request.getDurationMinutes() != null ? request.getDurationMinutes() : 45);
        schedule.setMode(request.getMode() != null ? request.getMode() : "ONLINE");
        schedule.setMeetingLink(request.getMeetingLink());
        schedule.setFeedback(request.getFeedback());
        schedule.setStatus(InterviewStatus.SCHEDULED);

        // Update application status to INTERVIEW_SCHEDULED
        app.setStatus(ApplicationStatus.INTERVIEW_SCHEDULED);
        applicationRepository.save(app);

        InterviewSchedule saved = interviewScheduleRepository.save(schedule);

        // Send notifications
        notificationService.sendNotification(
                app.getCandidate(),
                "Interview Scheduled: " + app.getJob().getTitle(),
                "Your interview with " + app.getJob().getCompanyName() + " is scheduled for " + scheduledTime.toString().replace("T", " at ") + ". Meeting Link: " + request.getMeetingLink(),
                NotificationType.INTERVIEW_SCHEDULED,
                "/dashboard/seeker"
        );

        if (app.getJob().getRecruiter() != null) {
            notificationService.sendNotification(
                    app.getJob().getRecruiter(),
                    "Interview Confirmed: " + app.getCandidate().getFullName(),
                    "Interview scheduled with " + app.getCandidate().getFullName() + " for " + app.getJob().getTitle() + ".",
                    NotificationType.INTERVIEW_SCHEDULED,
                    "/candidate-analysis/" + app.getId()
            );
        }

        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<InterviewScheduleDto> getMyInterviews(UserPrincipal principal) {
        User candidate = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return interviewScheduleRepository.findByCandidate(candidate).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InterviewScheduleDto> getRecruiterInterviews(UserPrincipal principal) {
        User recruiter = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return interviewScheduleRepository.findByRecruiter(recruiter).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public InterviewScheduleDto updateStatus(Long interviewId, InterviewStatus newStatus, String rescheduleTime) {
        InterviewSchedule schedule = interviewScheduleRepository.findById(interviewId)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found"));

        schedule.setStatus(newStatus);
        if (newStatus == InterviewStatus.RESCHEDULED && rescheduleTime != null && !rescheduleTime.isBlank()) {
            try {
                schedule.setScheduledAt(LocalDateTime.parse(rescheduleTime));
            } catch (Exception ignored) {}
        }

        InterviewSchedule saved = interviewScheduleRepository.save(schedule);

        // Notify Candidate
        NotificationType notifType = newStatus == InterviewStatus.CANCELLED ? NotificationType.INTERVIEW_CANCELLED : NotificationType.INTERVIEW_RESCHEDULED;
        notificationService.sendNotification(
                schedule.getApplication().getCandidate(),
                "Interview " + newStatus,
                "Your interview for " + schedule.getApplication().getJob().getTitle() + " has been " + newStatus.name().toLowerCase() + ".",
                notifType,
                "/dashboard/seeker"
        );

        return mapToDto(saved);
    }

    public InterviewScheduleDto mapToDto(InterviewSchedule s) {
        Application app = s.getApplication();
        return InterviewScheduleDto.builder()
                .id(s.getId())
                .applicationId(app.getId())
                .jobId(app.getJob().getId())
                .jobTitle(app.getJob().getTitle())
                .companyName(app.getJob().getCompanyName())
                .candidateName(app.getCandidate().getFullName())
                .candidateEmail(app.getCandidate().getEmail())
                .scheduledAt(s.getScheduledAt())
                .durationMinutes(s.getDurationMinutes())
                .mode(s.getMode())
                .meetingLink(s.getMeetingLink())
                .status(s.getStatus() != null ? s.getStatus().name() : "SCHEDULED")
                .feedback(s.getFeedback())
                .build();
    }
}
