package com.smarthire.service;

import com.smarthire.dto.UserDto;
import com.smarthire.dto.UserProfileDto;
import com.smarthire.entity.User;
import com.smarthire.entity.UserProfile;
import com.smarthire.repository.UserProfileRepository;
import com.smarthire.repository.UserRepository;
import com.smarthire.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional(readOnly = true)
    public UserDto getCurrentUserDto(UserPrincipal principal) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return mapToDto(user);
    }

    @Transactional(readOnly = true)
    public User getCurrentUser(UserPrincipal principal) {
        return userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional(readOnly = true)
    public UserProfileDto getProfile(UserPrincipal principal) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserProfile profile = user.getProfile();
        if (profile == null) {
            return UserProfileDto.builder()
                    .fullName(user.getFullName())
                    .phone(user.getPhone())
                    .skills(new ArrayList<>())
                    .build();
        }

        return mapToProfileDto(user, profile);
    }

    @Transactional
    public UserProfileDto updateProfile(UserPrincipal principal, UserProfileDto profileDto) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (profileDto.getFullName() != null && !profileDto.getFullName().isBlank()) {
            user.setFullName(profileDto.getFullName().trim());
        }
        if (profileDto.getPhone() != null) {
            user.setPhone(profileDto.getPhone().trim());
        }
        userRepository.save(user);

        UserProfile profile = user.getProfile();
        if (profile == null) {
            profile = UserProfile.builder().user(user).build();
            user.setProfile(profile);
        }

        profile.setHeadline(profileDto.getHeadline());
        profile.setSummary(profileDto.getSummary());
        profile.setCurrentLocation(profileDto.getCurrentLocation());
        profile.setTotalExperienceYears(profileDto.getTotalExperienceYears() != null ? profileDto.getTotalExperienceYears() : 0.0);
        profile.setExpectedSalary(profileDto.getExpectedSalary());
        profile.setNoticePeriodDays(profileDto.getNoticePeriodDays() != null ? profileDto.getNoticePeriodDays() : 30);
        profile.setEducation(profileDto.getEducation());
        profile.setExperienceDetails(profileDto.getExperienceDetails());
        profile.setGithubUrl(profileDto.getGithubUrl());
        profile.setLinkedinUrl(profileDto.getLinkedinUrl());
        profile.setPortfolioUrl(profileDto.getPortfolioUrl());

        if (profileDto.getSkills() != null) {
            profile.setSkills(new ArrayList<>(profileDto.getSkills()));
        }

        UserProfile saved = userProfileRepository.save(profile);
        return mapToProfileDto(user, saved);
    }

    @Transactional
    public UserDto updateRecruiterProfile(UserPrincipal principal, UserDto dto) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (dto.getFullName() != null && !dto.getFullName().isBlank()) {
            user.setFullName(dto.getFullName().trim());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone().trim());
        }
        if (dto.getCompanyName() != null) {
            user.setCompanyName(dto.getCompanyName().trim());
        }
        if (dto.getDesignation() != null) {
            user.setDesignation(dto.getDesignation().trim());
        }

        User saved = userRepository.save(user);
        return mapToDto(saved);
    }

    public UserProfileDto mapToProfileDto(User user, UserProfile profile) {
        return UserProfileDto.builder()
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .headline(profile.getHeadline())
                .summary(profile.getSummary())
                .currentLocation(profile.getCurrentLocation())
                .totalExperienceYears(profile.getTotalExperienceYears())
                .expectedSalary(profile.getExpectedSalary())
                .noticePeriodDays(profile.getNoticePeriodDays())
                .education(profile.getEducation())
                .experienceDetails(profile.getExperienceDetails())
                .githubUrl(profile.getGithubUrl())
                .linkedinUrl(profile.getLinkedinUrl())
                .portfolioUrl(profile.getPortfolioUrl())
                .skills(profile.getSkills() != null ? new ArrayList<>(profile.getSkills()) : new ArrayList<>())
                .build();
    }

    public UserDto mapToDto(User user) {
        UserProfile profile = user.getProfile();
        UserProfileDto profileDto = null;
        if (profile != null) {
            profileDto = mapToProfileDto(user, profile);
        }

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .companyName(user.getCompanyName())
                .designation(user.getDesignation())
                .profile(profileDto)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
