package com.example.webapp.controller;

import com.example.webapp.entity.Course;
import com.example.webapp.entity.Student;
import com.example.webapp.entity.Teacher;
import com.example.webapp.entity.User;
import com.example.webapp.repository.StudentRepository;
import com.example.webapp.repository.TeacherRepository;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.repository.UserRepository;
import com.example.webapp.service.CourseService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;


@Controller
@RequestMapping("/teacher/courses")
public class TeacherCourseController {

    private final CourseRepository courseRepository;
    private  final StudentRepository studentRepository;
    private final UserRepository  userRepository;
    private final TeacherRepository teacherRepository;
    private final CourseService courseService;

    public TeacherCourseController(CourseRepository courseRepository, TeacherRepository teacherRepository, CourseService courseService,  UserRepository userRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.courseService = courseService;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public String myCourses(Authentication auth, Model model) {

        String username = auth.getName();
        Teacher teacher = teacherRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));

        model.addAttribute("courses", courseService.getCoursesByTeacherId(teacher.getId()));

        return "teacher-courses";
    }

    @GetMapping("/new")
    public String newCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "teacher-course-new";
    }

    @PostMapping("/new")
    public String createCourse(@ModelAttribute Course course, Authentication auth) {

        String username = auth.getName();
        Teacher teacher = teacherRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));

        // attach logged-in teacher
        course.setTeacher(teacher);

        courseService.save(course);

        return "redirect:/teacher/courses";
    }


    @Transactional
    @PostMapping("/{id}/delete")
    public String deleteStudent(@PathVariable Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // ✅ remove join table links first
        student.getCourses().clear();

        // ✅ detach student from user first
        User user = student.getUser();
        student.setUser(null);
        studentRepository.save(student);

        // ✅ now delete student safely
        studentRepository.delete(student);

        // ✅ now delete user account (if existed)
        if (user != null) {
            userRepository.delete(user);
        }

        return "redirect:/teacher/students?deleted";
    }



}
