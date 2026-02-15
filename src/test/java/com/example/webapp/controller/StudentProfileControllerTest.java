package com.example.webapp.controller;

import com.example.webapp.entity.Role;
import com.example.webapp.entity.Student;
import com.example.webapp.entity.User;
import com.example.webapp.repository.StudentRepository;
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
class StudentProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Create user and student for testing
        User user = new User("student", "encoded", Role.STUDENT);
        user = userRepository.save(user);

        Student student = new Student();
        student.setName("Test Student");
        student.setRoll("2107001");
        student.setUser(user);
        studentRepository.save(student);
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void profile_ReturnsStudentProfilePage() throws Exception {
        mockMvc.perform(get("/student/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("student-profile"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void editProfile_ReturnsEditForm() throws Exception {
        mockMvc.perform(get("/student/profile/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("student-profile-edit"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void updateProfile_RedirectsToProfile() throws Exception {
        mockMvc.perform(post("/student/profile/edit")
                        .param("name", "Updated Name")
                        .param("roll", "2107099"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/profile?updated"));
    }
}