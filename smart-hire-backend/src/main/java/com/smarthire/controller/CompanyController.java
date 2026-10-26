package com.smarthire.controller;

import com.smarthire.dto.CompanyDto;
import com.smarthire.security.UserPrincipal;
import com.smarthire.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<CompanyDto> getCompanyByName(@PathVariable String name) {
        return ResponseEntity.ok(companyService.getCompanyByName(name));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<CompanyDto> createOrUpdateCompany(@AuthenticationPrincipal UserPrincipal principal,
                                                           @RequestBody CompanyDto companyDto) {
        return ResponseEntity.ok(companyService.createOrUpdateCompany(principal, companyDto));
    }
}
