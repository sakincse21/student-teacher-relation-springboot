package com.example.webapp.service;

import com.example.webapp.dto.StudentDTO;
import com.example.webapp.entity.Student;
import com.example.webapp.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        // Clean database before each test
        studentRepository.deleteAll();
    }

    @Test
    void getAllStudents_ReturnsAllStudents() {
        // Arrange
        Student s1 = new Student();
        s1.setName("Student 1");
        s1.setRoll("2107001");
        studentRepository.save(s1);

        Student s2 = new Student();
        s2.setName("Student 2");
        s2.setRoll("2107002");
        studentRepository.save(s2);

        // Act
        List<Student> result = studentService.getAllStudents();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getStudentById_Exists_ReturnsStudent() {
        // Arrange
        Student student = new Student();
        student.setName("Sakin");
        student.setRoll("2107010");
        Student saved = studentRepository.save(student);

        // Act
        Optional<Student> result = studentService.getStudentById(saved.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Sakin", result.get().getName());
    }

    @Test
    void getStudentById_NotExists_ReturnsEmpty() {
        // Act
        Optional<Student> result = studentService.getStudentById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void saveStudent_MapsAndSavesSuccessfully() {
        // Arrange
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setName("New Student");
        studentDTO.setRoll("2107099");

        // Act
        Student result = studentService.saveStudent(studentDTO);

        // Assert
        assertNotNull(result);
        assertEquals("New Student", result.getName());
        assertEquals("2107099", result.getRoll());
    }
}