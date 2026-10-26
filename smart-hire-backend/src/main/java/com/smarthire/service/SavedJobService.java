package com.smarthire.service;

import com.smarthire.dto.JobDto;
import com.smarthire.entity.Job;
import com.smarthire.entity.SavedJob;
import com.smarthire.entity.User;
import com.smarthire.repository.JobRepository;
import com.smarthire.repository.SavedJobRepository;
import com.smarthire.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;
    private final UserService userService;
    private final JobService jobService;

    public SavedJobService(SavedJobRepository savedJobRepository,
                           JobRepository jobRepository,
                           UserService userService,
                           JobService jobService) {
        this.savedJobRepository = savedJobRepository;
        this.jobRepository = jobRepository;
        this.userService = userService;
        this.jobService = jobService;
    }

    @Transactional
    public void saveJob(UserPrincipal principal, Long jobId) {
        User user = userService.getCurrentUser(principal);
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (!savedJobRepository.existsByUserAndJob(user, job)) {
            SavedJob savedJob = SavedJob.builder()
                    .user(user)
                    .job(job)
                    .build();
            savedJobRepository.save(savedJob);
        }
    }

    @Transactional(readOnly = true)
    public List<JobDto> getSavedJobs(UserPrincipal principal) {
        User user = userService.getCurrentUser(principal);
        return savedJobRepository.findByUserOrderBySavedAtDesc(user).stream()
                .map(sj -> jobService.mapToDto(sj.getJob()))
                .collect(Collectors.toList());
    }
}
