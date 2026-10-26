package com.smarthire.repository;

import com.smarthire.entity.Resume;
import com.smarthire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByUserOrderByCreatedAtDesc(User user);
    Optional<Resume> findFirstByUserAndIsPrimaryTrue(User user);
    List<Resume> findByUserId(Long userId);
}
