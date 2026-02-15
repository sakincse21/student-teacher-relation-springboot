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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class StudentCourseControllerTest {

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

        // Create user and student
        User user = new User("student", "encoded", Role.STUDENT);
        user = userRepository.save(user);

        Student student = new Student();
        student.setName("Test Student");
        student.setRoll("2107001");
        student.setUser(user);
        studentRepository.save(student);

        // Create teacher and course
        User teacherUser = new User("teacher", "encoded", Role.TEACHER);
        teacherUser = userRepository.save(teacherUser);

        Teacher teacher = new Teacher();
        teacher.setFullName("Test Teacher");
        teacher.setEmail("teacher@test.com");
        teacher.setUser(teacherUser);
        teacher = teacherRepository.save(teacher);

        Course course = new Course("CSE-101", "Test Course", teacher);
        courseRepository.save(course);
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void allCourses_ReturnsAllCoursesPage() throws Exception {
        mockMvc.perform(get("/student/all-courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("student-all-courses"))
                .andExpect(model().attributeExists("courses"));
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void myCourses_ReturnsMyCoursesPage() throws Exception {
        mockMvc.perform(get("/student/my-courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("student-my-courses"))
                .andExpect(model().attributeExists("courses"));
    }
}