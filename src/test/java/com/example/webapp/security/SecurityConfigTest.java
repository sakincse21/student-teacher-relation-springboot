package com.example.webapp.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginPage_AccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void cssResources_AccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/css/style.css"))
                .andExpect(status().isNotFound()); // Will return 404 but not 403
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void teacherDashboard_AccessibleByTeacher() throws Exception {
        mockMvc.perform(get("/teacher/dashboard"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void studentDashboard_AccessibleByStudent() throws Exception {
        mockMvc.perform(get("/student/dashboard"))
                .andExpect(status().isOk());
    }

    @Test
    void teacherDashboard_NotAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/teacher/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void teacherDashboard_NotAccessibleByStudent() throws Exception {
        mockMvc.perform(get("/teacher/dashboard"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void studentDashboard_NotAccessibleByTeacher() throws Exception {
        mockMvc.perform(get("/student/dashboard"))
                .andExpect(status().isForbidden());
    }
}