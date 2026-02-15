package com.example.webapp.controller;

import com.example.webapp.dto.StudentDTO;
import com.example.webapp.entity.Role;
import com.example.webapp.entity.User;
import com.example.webapp.repository.StudentRepository;
import com.example.webapp.repository.UserRepository;
import com.example.webapp.security.CustomUserDetailsService;
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
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void getStudents_ReturnsStudentsPage() throws Exception {
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attributeExists("students"));
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void addStudent_ReturnsStudentForm() throws Exception {
        mockMvc.perform(get("/students/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("student-form"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void storeStudent_RedirectsToStudents() throws Exception {
        mockMvc.perform(post("/students/store")
                        .param("name", "New Student")
                        .param("roll", "2107099"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));
    }
}