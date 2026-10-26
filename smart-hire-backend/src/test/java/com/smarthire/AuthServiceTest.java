package com.smarthire;

import com.smarthire.dto.AuthResponse;
import com.smarthire.dto.RegisterRequest;
import com.smarthire.entity.User;
import com.smarthire.entity.enums.Role;
import com.smarthire.entity.enums.UserStatus;
import com.smarthire.repository.UserRepository;
import com.smarthire.security.JwtTokenProvider;
import com.smarthire.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setFullName("Test User");
        registerRequest.setEmail("test@smarthire.ai");
        registerRequest.setPassword("password123");
        registerRequest.setRole(Role.JOB_SEEKER);
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.existsByEmail("test@smarthire.ai")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(tokenProvider.generateToken(any())).thenReturn("mockJwtToken");

        User savedUser = User.builder()
                .id(1L)
                .fullName("Test User")
                .email("test@smarthire.ai")
                .password("encodedPassword")
                .role(Role.JOB_SEEKER)
                .status(UserStatus.ACTIVE)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("mockJwtToken", response.getToken());
        assertEquals("test@smarthire.ai", response.getEmail());
        assertEquals(Role.JOB_SEEKER, response.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_DuplicateEmailThrowsException() {
        when(userRepository.existsByEmail("test@smarthire.ai")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            authService.register(registerRequest);
        });

        verify(userRepository, never()).save(any(User.class));
    }
}
