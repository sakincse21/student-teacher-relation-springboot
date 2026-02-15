package com.example.webapp.repository;

import com.example.webapp.entity.Teacher;
import com.example.webapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        teacherRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void saveTeacher_PersistsSuccessfully() {
        // Save user first
        User user = new User("teacher1", "encoded", com.example.webapp.entity.Role.TEACHER);
        user = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setFullName("Test Teacher");
        teacher.setEmail("teacher@test.com");
        teacher.setUser(user);

        Teacher savedTeacher = teacherRepository.save(teacher);

        assertNotNull(savedTeacher.getId());
        assertEquals("Test Teacher", savedTeacher.getFullName());
    }

    @Test
    void findByUserUsername_Exists_ReturnsTeacher() {
        // Save user first
        User user = new User("findteacher", "encoded", com.example.webapp.entity.Role.TEACHER);
        user = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setFullName("Find Me");
        teacher.setEmail("find@test.com");
        teacher.setUser(user);
        teacherRepository.save(teacher);

        Optional<Teacher> result = teacherRepository.findByUserUsername("findteacher");

        assertTrue(result.isPresent());
        assertEquals("Find Me", result.get().getFullName());
    }

    @Test
    void findByUserUsername_NotExists_ReturnsEmpty() {
        Optional<Teacher> result = teacherRepository.findByUserUsername("nonexistent");

        assertFalse(result.isPresent());
    }
}