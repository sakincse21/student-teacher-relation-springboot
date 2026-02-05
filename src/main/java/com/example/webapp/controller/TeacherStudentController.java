package com.example.webapp.controller;

import com.example.webapp.dto.StudentCreateRequest;
import com.example.webapp.entity.Role;
import com.example.webapp.entity.Student;
import com.example.webapp.entity.User;
import com.example.webapp.repository.StudentRepository;
import com.example.webapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teacher/students")
public class TeacherStudentController {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TeacherStudentController(StudentRepository studentRepository,
                                    UserRepository userRepository,
                                    PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "teacher-students";
    }

    @GetMapping("/new")
    public String newStudentForm(Model model) {
        model.addAttribute("req", new StudentCreateRequest());
        return "teacher-student-new";
    }

    @PostMapping("/new")
    public String createStudent(@ModelAttribute("req") StudentCreateRequest req) {

        Student student = new Student();
        student.setName(req.getName());
        student.setRoll(req.getRoll());

        // ✅ optional: create login account for student
        if (req.getUsername() != null && !req.getUsername().isBlank()
                && req.getPassword() != null && !req.getPassword().isBlank()) {

            // username must be unique
            if (userRepository.findByUsername(req.getUsername()).isPresent()) {
                throw new RuntimeException("Username already exists!");
            }

            User user = new User();
            user.setUsername(req.getUsername());
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            user.setRole(Role.STUDENT);

            userRepository.save(user);

            student.setUser(user);
        }

        studentRepository.save(student);

        return "redirect:/teacher/students?success";
    }

    @PostMapping("/{id}/delete")
    public String deleteStudent(@PathVariable Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // ✅ remove courses first (avoid FK errors)
        student.getCourses().clear();
        studentRepository.save(student);

        if (student.getUser() != null) {
            userRepository.delete(student.getUser());
        }

        studentRepository.delete(student);

        return "redirect:/teacher/students?deleted";
    }

}
