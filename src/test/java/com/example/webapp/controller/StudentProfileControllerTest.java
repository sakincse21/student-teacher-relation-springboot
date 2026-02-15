package com.example.webapp.controller;

import com.example.webapp.entity.Student;
import com.example.webapp.entity.User;
import com.example.webapp.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentProfileController.class)
class StudentProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void profile_ReturnsStudentProfilePage() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Test Student");
        student.setRoll("2107001");

        when(studentRepository.findByUserUsername("student")).thenReturn(Optional.of(student));

        mockMvc.perform(get("/student/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("student-profile"))
                .andExpect(model().attribute("student", student));

        verify(studentRepository, times(1)).findByUserUsername("student");
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void editProfile_ReturnsEditForm() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Test Student");
        student.setRoll("2107001");

        when(studentRepository.findByUserUsername("student")).thenReturn(Optional.of(student));

        mockMvc.perform(get("/student/profile/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("student-profile-edit"))
                .andExpect(model().attributeExists("student"));

        verify(studentRepository, times(1)).findByUserUsername("student");
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void updateProfile_RedirectsToProfile() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Old Name");
        student.setRoll("2107001");

        when(studentRepository.findByUserUsername("student")).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student/profile/edit")
                        .param("name", "Updated Name")
                        .param("roll", "2107099"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/profile?updated"));

        verify(studentRepository, times(1)).findByUserUsername("student");
        verify(studentRepository, times(1)).save(any(Student.class));
    }
}