package com.example.webapp.controller;

import com.example.webapp.entity.Role;
import com.example.webapp.entity.Teacher;
import com.example.webapp.entity.User;
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
class TeacherStudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
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
    void listStudents_ReturnsStudentsPage() throws Exception {
        mockMvc.perform(get("/teacher/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher-students"))
                .andExpect(model().attributeExists("students"));
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void newStudentForm_ReturnsStudentForm() throws Exception {
        mockMvc.perform(get("/teacher/students/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher-student-new"))
                .andExpect(model().attributeExists("req"));
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void createStudent_RedirectsToStudents() throws Exception {
        mockMvc.perform(post("/teacher/students/new")
                        .param("name", "New Student")
                        .param("roll", "2107099")
                        .param("username", "newstudent")
                        .param("password", "student123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher/students?success"));
    }
}