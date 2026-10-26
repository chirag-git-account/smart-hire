package com.smarthire.controller;

import com.smarthire.dto.AdminStatsDto;
import com.smarthire.dto.ApplicationDto;
import com.smarthire.dto.CompanyDto;
import com.smarthire.dto.JobDto;
import com.smarthire.dto.UserDto;
import com.smarthire.entity.enums.ApplicationStatus;
import com.smarthire.entity.enums.UserStatus;
import com.smarthire.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDto> getAdminStats() {
        return ResponseEntity.ok(adminService.getAdminStats());
    }

    // ==========================================
    // USER MANAGEMENT
    // ==========================================
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PatchMapping("/users/{id}/status")
    public ResponseEntity<UserDto> updateUserStatus(@PathVariable Long id,
                                                    @RequestParam UserStatus status) {
        return ResponseEntity.ok(adminService.updateUserStatus(id, status));
    }

    // ==========================================
    // 1. ADMIN COMPANY MANAGEMENT
    // ==========================================
    @GetMapping("/companies")
    public ResponseEntity<Page<CompanyDto>> getCompanies(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(adminService.getCompanies(keyword, active, page, size));
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getCompanyById(id));
    }

    @PatchMapping("/companies/{id}/status")
    public ResponseEntity<CompanyDto> updateCompanyStatus(
            @PathVariable Long id,
            @RequestParam Boolean active) {
        return ResponseEntity.ok(adminService.updateCompanyStatus(id, active));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Map<String, String>> deleteCompany(@PathVariable Long id) {
        adminService.deleteCompany(id);
        return ResponseEntity.ok(Map.of("message", "Company deleted successfully"));
    }

    // ==========================================
    // 2. ADMIN JOB MANAGEMENT
    // ==========================================
    @GetMapping("/jobs")
    public ResponseEntity<Page<JobDto>> getJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(adminService.getJobs(keyword, company, active, page, size));
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<JobDto> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getJobById(id));
    }

    @PatchMapping("/jobs/{id}/status")
    public ResponseEntity<JobDto> updateJobStatus(
            @PathVariable Long id,
            @RequestParam Boolean active) {
        return ResponseEntity.ok(adminService.updateJobStatus(id, active));
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Map<String, String>> deleteJob(@PathVariable Long id) {
        adminService.deleteJob(id);
        return ResponseEntity.ok(Map.of("message", "Job deactivated/removed successfully"));
    }

    // ==========================================
    // 3. ADMIN APPLICATION MANAGEMENT
    // ==========================================
    @GetMapping("/applications")
    public ResponseEntity<Page<ApplicationDto>> getApplications(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(adminService.getApplications(keyword, status, page, size));
    }

    @GetMapping("/applications/{id}")
    public ResponseEntity<ApplicationDto> getApplicationById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getApplicationById(id));
    }

    @PatchMapping("/applications/{id}/status")
    public ResponseEntity<ApplicationDto> updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status) {
        return ResponseEntity.ok(adminService.updateApplicationStatus(id, status));
    }
}
