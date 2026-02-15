package com.example.webapp.controller;

import com.example.webapp.entity.Course;
import com.example.webapp.entity.Role;
import com.example.webapp.entity.Teacher;
import com.example.webapp.entity.User;
import com.example.webapp.repository.CourseRepository;
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
class TeacherCourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();

        // Create teacher user and profile
        User user = new User("teacher", "encoded", Role.TEACHER);
        user = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setFullName("Test Teacher");
        teacher.setEmail("teacher@test.com");
        teacher.setUser(user);
        teacherRepository.save(teacher);
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void myCourses_ReturnsTeacherCoursesPage() throws Exception {
        mockMvc.perform(get("/teacher/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher-courses"))
                .andExpect(model().attributeExists("courses"));
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void newCourseForm_ReturnsNewCourseForm() throws Exception {
        mockMvc.perform(get("/teacher/courses/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher-course-new"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void createCourse_RedirectsToCourses() throws Exception {
        mockMvc.perform(post("/teacher/courses/new")
                        .param("courseCode", "CSE-101")
                        .param("courseTitle", "New Course"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher/courses"));
    }
}