package com.example.webapp.controller;

import com.example.webapp.entity.*;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.repository.StudentRepository;
import com.example.webapp.repository.TeacherRepository;
import com.example.webapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TeacherAssignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();

        // Create teacher
        User teacherUser = new User("teacher", "encoded", Role.TEACHER);
        teacherUser = userRepository.save(teacherUser);

        Teacher teacher = new Teacher();
        teacher.setFullName("Test Teacher");
        teacher.setEmail("teacher@test.com");
        teacher.setUser(teacherUser);
        teacher = teacherRepository.save(teacher);

        // Create course
        Course course = new Course("CSE-101", "Test Course", teacher);
        courseRepository.save(course);

        // Create student
        User studentUser = new User("student", "encoded", Role.STUDENT);
        studentUser = userRepository.save(studentUser);

        Student student = new Student();
        student.setName("Test Student");
        student.setRoll("2107001");
        student.setUser(studentUser);
        studentRepository.save(student);
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void assignPage_ReturnsAssignPage() throws Exception {
        mockMvc.perform(get("/teacher/assign"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher-assign-course"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("students"));
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void assignCourse_RedirectsToAssign() throws Exception {
        mockMvc.perform(post("/teacher/assign")
                        .param("studentId", "1")
                        .param("courseId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher/assign?success"));
    }
}