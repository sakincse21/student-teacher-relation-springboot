package com.example.webapp.controller;

import com.example.webapp.dto.StudentDTO;
import com.example.webapp.entity.Student;
import com.example.webapp.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void getStudents_ReturnsStudentsPage() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student() {{ setName("Test"); setRoll("2107001"); }});
        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attributeExists("students"));

        verify(studentService, times(1)).getAllStudents();
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
        when(studentService.saveStudent(any(StudentDTO.class))).thenReturn(new Student());

        mockMvc.perform(post("/students/store")
                        .param("name", "New Student")
                        .param("roll", "2107099"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentService, times(1)).saveStudent(any(StudentDTO.class));
    }
}