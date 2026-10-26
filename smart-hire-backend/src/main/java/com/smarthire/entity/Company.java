package com.smarthire.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String logoUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String industry;

    private String location;

    private String website;

    private String employeeSize;

    private Integer foundedYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Company() {}

    public Company(Long id, String name, String logoUrl, String description, String industry, String location, String website, String employeeSize, Integer foundedYear, User owner, Boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.description = description;
        this.industry = industry;
        this.location = location;
        this.website = website;
        this.employeeSize = employeeSize;
        this.foundedYear = foundedYear;
        this.owner = owner;
        this.isActive = isActive != null ? isActive : true;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private String logoUrl;
        private String description;
        private String industry;
        private String location;
        private String website;
        private String employeeSize;
        private Integer foundedYear;
        private User owner;
        private Boolean isActive = true;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder logoUrl(String logoUrl) { this.logoUrl = logoUrl; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder industry(String industry) { this.industry = industry; return this; }
        public Builder location(String location) { this.location = location; return this; }
        public Builder website(String website) { this.website = website; return this; }
        public Builder employeeSize(String employeeSize) { this.employeeSize = employeeSize; return this; }
        public Builder foundedYear(Integer foundedYear) { this.foundedYear = foundedYear; return this; }
        public Builder owner(User owner) { this.owner = owner; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Company build() {
            return new Company(id, name, logoUrl, description, industry, location, website, employeeSize, foundedYear, owner, isActive, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public String getEmployeeSize() { return employeeSize; }
    public void setEmployeeSize(String employeeSize) { this.employeeSize = employeeSize; }
    public Integer getFoundedYear() { return foundedYear; }
    public void setFoundedYear(Integer foundedYear) { this.foundedYear = foundedYear; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
