package com.example.webapp.controller;

import com.example.webapp.entity.Course;
import com.example.webapp.entity.Teacher;
import com.example.webapp.entity.User;
import com.example.webapp.repository.TeacherRepository;
import com.example.webapp.service.CourseService;
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

@WebMvcTest(TeacherCourseController.class)
class TeacherCourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private TeacherRepository teacherRepository;

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void myCourses_ReturnsTeacherCoursesPage() throws Exception {
        User user = new User("teacher", "encoded", com.example.webapp.entity.Role.TEACHER);
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setUser(user);

        List<Course> courses = new ArrayList<>();
        courses.add(new Course("CSE-101", "Test Course", teacher));

        when(teacherRepository.findByUserUsername("teacher")).thenReturn(Optional.of(teacher));
        when(courseService.getCoursesByTeacherId(1L)).thenReturn(courses);

        mockMvc.perform(get("/teacher/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher-courses"))
                .andExpect(model().attributeExists("courses"));

        verify(courseService, times(1)).getCoursesByTeacherId(1L);
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
        User user = new User("teacher", "encoded", com.example.webapp.entity.Role.TEACHER);
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setUser(user);

        when(teacherRepository.findByUserUsername("teacher")).thenReturn(Optional.of(teacher));
        when(courseService.save(any(Course.class))).thenReturn(new Course());

        mockMvc.perform(post("/teacher/courses/new")
                        .param("courseCode", "CSE-101")
                        .param("courseTitle", "New Course"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher/courses"));

        verify(courseService, times(1)).save(any(Course.class));
    }
}