package com.example.webapp.controller;

import com.example.webapp.entity.Course;
import com.example.webapp.entity.Student;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.repository.StudentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentCourseController {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public StudentCourseController(CourseRepository courseRepository,
                                   StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    // ✅ student can view all courses
    @GetMapping("/all-courses")
    public String allCourses(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "student-all-courses";
    }

    // ✅ student can view own assigned courses
    @GetMapping("/my-courses")
    public String myCourses(Authentication auth, Model model) {

        String username = auth.getName();

        Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Student profile not linked with this user"));

        model.addAttribute("courses", student.getCourses());

        return "student-my-courses";
    }
}
