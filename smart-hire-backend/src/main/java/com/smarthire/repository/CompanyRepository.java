package com.smarthire.repository;

import com.smarthire.entity.Company;
import com.smarthire.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByNameIgnoreCase(String name);
    List<Company> findByOwner(User owner);
    List<Company> findByIndustryIgnoreCase(String industry);

    @Query("SELECT c FROM Company c WHERE " +
           "(:isActive IS NULL OR c.isActive = :isActive) AND " +
           "(:keyword IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.industry) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Company> searchCompaniesWithStatus(@Param("keyword") String keyword, @Param("isActive") Boolean isActive, Pageable pageable);
}
