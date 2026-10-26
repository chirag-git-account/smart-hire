package com.smarthire.dto;

import com.smarthire.entity.enums.Role;
import com.smarthire.entity.enums.UserStatus;

import java.time.LocalDateTime;

public class UserDto {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private Role role;
    private UserStatus status;
    private String companyName;
    private String designation;
    private UserProfileDto profile;
    private LocalDateTime createdAt;

    public UserDto() {}

    public UserDto(Long id, String email, String fullName, String phone, Role role, UserStatus status, String companyName, String designation, UserProfileDto profile, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.companyName = companyName;
        this.designation = designation;
        this.profile = profile;
        this.createdAt = createdAt;
    }

    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    public static class UserDtoBuilder {
        private Long id;
        private String email;
        private String fullName;
        private String phone;
        private Role role;
        private UserStatus status;
        private String companyName;
        private String designation;
        private UserProfileDto profile;
        private LocalDateTime createdAt;

        public UserDtoBuilder id(Long id) { this.id = id; return this; }
        public UserDtoBuilder email(String email) { this.email = email; return this; }
        public UserDtoBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public UserDtoBuilder phone(String phone) { this.phone = phone; return this; }
        public UserDtoBuilder role(Role role) { this.role = role; return this; }
        public UserDtoBuilder status(UserStatus status) { this.status = status; return this; }
        public UserDtoBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public UserDtoBuilder designation(String designation) { this.designation = designation; return this; }
        public UserDtoBuilder profile(UserProfileDto profile) { this.profile = profile; return this; }
        public UserDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public UserDto build() {
            return new UserDto(id, email, fullName, phone, role, status, companyName, designation, profile, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public UserProfileDto getProfile() { return profile; }
    public void setProfile(UserProfileDto profile) { this.profile = profile; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
