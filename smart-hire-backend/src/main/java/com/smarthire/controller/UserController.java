package com.smarthire.controller;

import com.smarthire.dto.UserDto;
import com.smarthire.dto.UserProfileDto;
import com.smarthire.security.UserPrincipal;
import com.smarthire.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.getCurrentUserDto(principal));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.getProfile(principal));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileDto> updateProfile(@AuthenticationPrincipal UserPrincipal principal,
                                                        @RequestBody UserProfileDto profileDto) {
        return ResponseEntity.ok(userService.updateProfile(principal, profileDto));
    }

    @PutMapping("/recruiter-profile")
    public ResponseEntity<UserDto> updateRecruiterProfile(@AuthenticationPrincipal UserPrincipal principal,
                                                          @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateRecruiterProfile(principal, userDto));
    }
}
