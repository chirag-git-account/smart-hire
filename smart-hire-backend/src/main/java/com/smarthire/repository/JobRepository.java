package com.smarthire.repository;

import com.smarthire.entity.Job;
import com.smarthire.entity.User;
import com.smarthire.entity.enums.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByRecruiter(User recruiter);
    List<Job> findByIsActiveTrueOrderByCreatedAtDesc();
    List<Job> findByCompanyNameIgnoreCase(String companyName);
    List<Job> findByCompanyNameIgnoreCaseAndIsActiveTrueOrderByCreatedAtDesc(String companyName);

    @Query("SELECT j FROM Job j WHERE j.isActive = true AND " +
           "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(j.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:jobType IS NULL OR j.jobType = :jobType)")
    Page<Job> searchJobs(@Param("keyword") String keyword,
                         @Param("location") String location,
                         @Param("jobType") JobType jobType,
                         Pageable pageable);

    @Query("SELECT j FROM Job j WHERE " +
           "(:isActive IS NULL OR j.isActive = :isActive) AND " +
           "(:companyName IS NULL OR LOWER(j.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))) AND " +
           "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Job> adminSearchJobs(@Param("keyword") String keyword,
                              @Param("companyName") String companyName,
                              @Param("isActive") Boolean isActive,
                              Pageable pageable);

    Long countByIsActiveTrue();
    Long countByCompanyNameIgnoreCase(String companyName);
}
