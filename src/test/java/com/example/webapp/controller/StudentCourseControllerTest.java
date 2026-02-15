package com.example.webapp.controller;

import com.example.webapp.entity.Course;
import com.example.webapp.entity.Student;
import com.example.webapp.entity.Teacher;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentCourseController.class)
class StudentCourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void allCourses_ReturnsAllCoursesPage() throws Exception {
        // Arrange
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("CSE-101", "Intro", new Teacher()));
        when(courseRepository.findAll()).thenReturn(courses);

        // Act & Assert
        mockMvc.perform(get("/student/all-courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("student-all-courses"))
                .andExpect(model().attributeExists("courses"));

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void myCourses_ReturnsMyCoursesPage() throws Exception {
        // Arrange
        Student student = new Student();
        student.setName("Test Student");
        student.setRoll("2107001");
        student.setCourses(new java.util.HashSet<>());

        when(studentRepository.findByUserUsername("student")).thenReturn(Optional.of(student));

        // Act & Assert
        mockMvc.perform(get("/student/my-courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("student-my-courses"))
                .andExpect(model().attributeExists("courses"));

        verify(studentRepository, times(1)).findByUserUsername("student");
    }
}