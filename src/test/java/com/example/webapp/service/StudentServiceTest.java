package com.example.webapp.service;

import com.example.webapp.dto.StudentDTO;
import com.example.webapp.entity.Student;
import com.example.webapp.repository.StudentRepository;
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

    @Test
    void getAllStudents_ReturnsAllStudents() {
        Student s1 = new Student();
        s1.setName("Student 1");
        s1.setRoll("2107001");
        studentRepository.save(s1);

        Student s2 = new Student();
        s2.setName("Student 2");
        s2.setRoll("2107002");
        studentRepository.save(s2);

        List<Student> result = studentService.getAllStudents();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getStudentById_Exists_ReturnsStudent() {
        Student student = new Student();
        student.setName("Sakin");
        student.setRoll("2107010");
        Student saved = studentRepository.save(student);

        Optional<Student> result = studentService.getStudentById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("Sakin", result.get().getName());
    }

    @Test
    void getStudentById_NotExists_ReturnsEmpty() {
        Optional<Student> result = studentService.getStudentById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void saveStudent_MapsAndSavesSuccessfully() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setName("New Student");
        studentDTO.setRoll("2107099");

        Student result = studentService.saveStudent(studentDTO);

        assertNotNull(result);
        assertEquals("New Student", result.getName());
        assertEquals("2107099", result.getRoll());
    }
}