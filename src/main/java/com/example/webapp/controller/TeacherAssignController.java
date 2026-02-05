package com.example.webapp.controller;

import com.example.webapp.entity.Course;
import com.example.webapp.entity.Student;
import com.example.webapp.entity.Teacher;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.repository.StudentRepository;
import com.example.webapp.repository.TeacherRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teacher/assign")
public class TeacherAssignController {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public TeacherAssignController(TeacherRepository teacherRepository,
                                   CourseRepository courseRepository,
                                   StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public String assignPage(Authentication auth, Model model) {

        String username = auth.getName();

        Teacher teacher = teacherRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));

        List<Course> myCourses = courseRepository.findByTeacherId(teacher.getId());
        List<Student> students = studentRepository.findAll();

        model.addAttribute("courses", myCourses);
        model.addAttribute("students", students);

        return "teacher-assign-course";
    }

    @PostMapping
    public String assignCourse(@RequestParam Long studentId,
                               @RequestParam Long courseId,
                               Authentication auth) {

        String username = auth.getName();

        Teacher teacher = teacherRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // ✅ security check: teacher can assign only their own course
        if (!course.getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("You cannot assign someone else's course!");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // ✅ assign
        student.getCourses().add(course);

        studentRepository.save(student);

        return "redirect:/teacher/assign?success";
    }
}
