package com.example.webapp.controller;

import com.example.webapp.dto.StudentCreateRequest;
import com.example.webapp.entity.Student;
import com.example.webapp.entity.User;
import com.example.webapp.repository.StudentRepository;
import com.example.webapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeacherStudentController.class)
class TeacherStudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void listStudents_ReturnsStudentsPage() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student() {{ setName("Test"); setRoll("2107001"); }});
        when(studentRepository.findAll()).thenReturn(students);

        mockMvc.perform(get("/teacher/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher-students"))
                .andExpect(model().attributeExists("students"));

        verify(studentRepository, times(1)).findAll();
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
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(studentRepository.save(any(Student.class))).thenReturn(new Student());

        mockMvc.perform(post("/teacher/students/new")
                        .param("name", "New Student")
                        .param("roll", "2107099"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher/students?success"));
    }
}