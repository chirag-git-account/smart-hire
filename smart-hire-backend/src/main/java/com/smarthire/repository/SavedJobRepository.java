package com.smarthire.repository;

import com.smarthire.entity.Job;
import com.smarthire.entity.SavedJob;
import com.smarthire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    List<SavedJob> findByUserOrderBySavedAtDesc(User user);
    Optional<SavedJob> findByUserAndJob(User user, Job job);
    Boolean existsByUserAndJob(User user, Job job);
    void deleteByUserAndJob(User user, Job job);
}
