package com.example.webapp.controller;

import com.example.webapp.entity.Course;
import com.example.webapp.entity.Student;
import com.example.webapp.entity.Teacher;
import com.example.webapp.entity.User;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.repository.StudentRepository;
import com.example.webapp.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeacherAssignController.class)
class TeacherAssignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherRepository teacherRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void assignPage_ReturnsAssignPage() throws Exception {
        User user = new User("teacher", "encoded", com.example.webapp.entity.Role.TEACHER);
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setUser(user);

        List<Course> courses = new ArrayList<>();
        courses.add(new Course("CSE-101", "Test Course", teacher));
        
        List<Student> students = new ArrayList<>();
        students.add(new Student() {{ setName("Test"); setRoll("2107001"); }});

        when(teacherRepository.findByUserUsername("teacher")).thenReturn(Optional.of(teacher));
        when(courseRepository.findByTeacherId(1L)).thenReturn(courses);
        when(studentRepository.findAll()).thenReturn(students);

        mockMvc.perform(get("/teacher/assign"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher-assign-course"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("students"));
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void assignCourse_RedirectsToAssign() throws Exception {
        User user = new User("teacher", "encoded", com.example.webapp.entity.Role.TEACHER);
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setUser(user);

        Course course = new Course("CSE-101", "Test Course", teacher);
        Student student = new Student();
        student.setName("Test Student");
        student.setRoll("2107001");

        when(teacherRepository.findByUserUsername("teacher")).thenReturn(Optional.of(teacher));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/teacher/assign")
                        .param("studentId", "1")
                        .param("courseId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher/assign?success"));

        verify(studentRepository, times(1)).save(any(Student.class));
    }
}