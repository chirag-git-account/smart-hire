package com.smarthire.repository;

import com.smarthire.entity.Application;
import com.smarthire.entity.InterviewSchedule;
import com.smarthire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long> {
    Optional<InterviewSchedule> findByApplication(Application application);
    Optional<InterviewSchedule> findByApplicationId(Long applicationId);

    @Query("SELECT i FROM InterviewSchedule i WHERE i.application.candidate = :candidate ORDER BY i.scheduledAt ASC")
    List<InterviewSchedule> findByCandidate(@Param("candidate") User candidate);

    @Query("SELECT i FROM InterviewSchedule i WHERE i.application.job.recruiter = :recruiter ORDER BY i.scheduledAt ASC")
    List<InterviewSchedule> findByRecruiter(@Param("recruiter") User recruiter);
}
