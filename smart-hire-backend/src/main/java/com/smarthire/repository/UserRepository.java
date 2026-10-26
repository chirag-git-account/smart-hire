package com.smarthire.repository;

import com.smarthire.entity.User;
import com.smarthire.entity.enums.Role;
import com.smarthire.entity.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    List<User> findByRole(Role role);
    Long countByRole(Role role);
    Long countByStatus(UserStatus status);

    @Query("SELECT COUNT(DISTINCT u.companyName) FROM User u WHERE u.role = 'RECRUITER' AND u.companyName IS NOT NULL")
    Long countDistinctCompanies();
}
