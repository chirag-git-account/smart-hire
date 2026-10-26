package com.smarthire.repository;

import com.smarthire.entity.Application;
import com.smarthire.entity.Job;
import com.smarthire.entity.User;
import com.smarthire.entity.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByCandidateOrderByAppliedAtDesc(User candidate);
    List<Application> findByJob(Job job);
    List<Application> findByJobId(Long jobId);
    Optional<Application> findByJobAndCandidate(Job job, User candidate);
    Boolean existsByJobAndCandidate(Job job, User candidate);

    Long countByCandidate(User candidate);
    Long countByCandidateAndStatus(User candidate, ApplicationStatus status);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.recruiter = :recruiter")
    Long countTotalApplicationsForRecruiter(@Param("recruiter") User recruiter);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.recruiter = :recruiter AND a.aiAnalysis.overallScore >= 80")
    Long countHighMatchApplicationsForRecruiter(@Param("recruiter") User recruiter);

    Long countByJob(Job job);

    @Query("SELECT a FROM Application a WHERE " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:keyword IS NULL OR LOWER(a.candidate.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.candidate.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.job.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.job.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Application> searchApplications(@Param("status") ApplicationStatus status,
                                         @Param("keyword") String keyword,
                                         Pageable pageable);
}
