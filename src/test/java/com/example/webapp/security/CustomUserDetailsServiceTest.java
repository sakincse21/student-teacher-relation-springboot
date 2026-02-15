package com.example.webapp.security;

import com.example.webapp.entity.Role;
import com.example.webapp.entity.User;
import com.example.webapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CustomUserDetailsServiceTest {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Clean database before each test
        userRepository.deleteAll();
        
        // Create test user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("{bcrypt}$2a$10$X64I5Z5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q"); // BCrypt encoded "password"
        user.setRole(Role.STUDENT);
        userRepository.save(user);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT")));
        assertFalse(userDetails.getPassword().isEmpty());
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        // Act & Assert
        assertThrows(UsernameNotFoundException.class, 
            () -> userDetailsService.loadUserByUsername("nonexistent"));
    }

    @Test
    void loadUserByUsername_TeacherUser_ReturnsTeacherRole() {
        // Arrange - create teacher user
        User teacherUser = new User();
        teacherUser.setUsername("teacheruser");
        teacherUser.setPassword("{bcrypt}$2a$10$X64I5Z5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q5q");
        teacherUser.setRole(Role.TEACHER);
        userRepository.save(teacherUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("teacheruser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("teacheruser", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER")));
    }

    @Test
    void loadUserByUsername_CaseInsensitive_ThrowsExceptionForWrongCase() {
        // Act & Assert - Spring Security is case-sensitive by default
        assertThrows(UsernameNotFoundException.class, 
            () -> userDetailsService.loadUserByUsername("TESTUSER"));
    }
}