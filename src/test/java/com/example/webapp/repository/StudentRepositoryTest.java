package com.example.webapp.repository;

import com.example.webapp.entity.Student;
import com.example.webapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveStudent_PersistsSuccessfully() {
        Student student = new Student();
        student.setName("Test Student");
        student.setRoll("2107099");

        Student savedStudent = studentRepository.save(student);

        assertNotNull(savedStudent.getId());
        assertEquals("Test Student", savedStudent.getName());
    }

    @Test
    void findAll_ReturnsAllStudents() {
        Student s1 = new Student();
        s1.setName("Student 1");
        s1.setRoll("2107001");
        
        Student s2 = new Student();
        s2.setName("Student 2");
        s2.setRoll("2107002");
        
        studentRepository.save(s1);
        studentRepository.save(s2);

        List<Student> students = studentRepository.findAll();

        assertNotNull(students);
        assertEquals(2, students.size());
    }

    @Test
    void findByUserUsername_Exists_ReturnsStudent() {
        // Save user first
        User user = new User("testuser", "encoded", com.example.webapp.entity.Role.STUDENT);
        user = userRepository.save(user);

        Student student = new Student();
        student.setName("Find Me");
        student.setRoll("2107003");
        student.setUser(user);
        studentRepository.save(student);

        Optional<Student> result = studentRepository.findByUserUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("Find Me", result.get().getName());
    }

    @Test
    void findByUserUsername_NotExists_ReturnsEmpty() {
        Optional<Student> result = studentRepository.findByUserUsername("nonexistent");

        assertFalse(result.isPresent());
    }
}