package com.smarthire.repository;

import com.smarthire.entity.AiAnalysis;
import com.smarthire.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AiAnalysisRepository extends JpaRepository<AiAnalysis, Long> {
    Optional<AiAnalysis> findByApplication(Application application);
    Optional<AiAnalysis> findByApplicationId(Long applicationId);
    Long countByOverallScoreGreaterThanEqual(Integer score);
}
