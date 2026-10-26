package com.smarthire.dto;

import com.smarthire.entity.enums.Role;
import com.smarthire.entity.enums.UserStatus;

public class AuthResponse {
    private String token;
    private Long id;
    private String email;
    private String fullName;
    private Role role;
    private UserStatus status;
    private String companyName;
    private String designation;

    public AuthResponse() {}

    public AuthResponse(String token, Long id, String email, String fullName, Role role, UserStatus status, String companyName, String designation) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.status = status;
        this.companyName = companyName;
        this.designation = designation;
    }

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    public static class AuthResponseBuilder {
        private String token;
        private Long id;
        private String email;
        private String fullName;
        private Role role;
        private UserStatus status;
        private String companyName;
        private String designation;

        public AuthResponseBuilder token(String token) { this.token = token; return this; }
        public AuthResponseBuilder id(Long id) { this.id = id; return this; }
        public AuthResponseBuilder email(String email) { this.email = email; return this; }
        public AuthResponseBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public AuthResponseBuilder role(Role role) { this.role = role; return this; }
        public AuthResponseBuilder status(UserStatus status) { this.status = status; return this; }
        public AuthResponseBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public AuthResponseBuilder designation(String designation) { this.designation = designation; return this; }

        public AuthResponse build() {
            return new AuthResponse(token, id, email, fullName, role, status, companyName, designation);
        }
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
}
