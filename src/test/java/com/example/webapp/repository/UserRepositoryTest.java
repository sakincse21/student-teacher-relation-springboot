package com.example.webapp.repository;

import com.example.webapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveUser_PersistsSuccessfully() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedpassword");
        user.setRole(com.example.webapp.entity.Role.STUDENT);

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());
    }

    @Test
    void findByUsername_Exists_ReturnsUser() {
        User user = new User("findme", "pass", com.example.webapp.entity.Role.TEACHER);
        userRepository.save(user);

        Optional<User> result = userRepository.findByUsername("findme");

        assertTrue(result.isPresent());
        assertEquals("findme", result.get().getUsername());
    }

    @Test
    void findByUsername_NotExists_ReturnsEmpty() {
        Optional<User> result = userRepository.findByUsername("nonexistent");

        assertFalse(result.isPresent());
    }
}