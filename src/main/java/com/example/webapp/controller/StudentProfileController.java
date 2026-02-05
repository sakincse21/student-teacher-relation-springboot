package com.example.webapp.controller;

import com.example.webapp.entity.Student;
import com.example.webapp.repository.StudentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student/profile")
public class StudentProfileController {

    private final StudentRepository studentRepository;

    public StudentProfileController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // ✅ view own profile
    @GetMapping
    public String profile(Authentication auth, Model model) {

        String username = auth.getName();

        Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Student profile not linked with this user"));

        model.addAttribute("student", student);

        return "student-profile";
    }

    // ✅ edit form
    @GetMapping("/edit")
    public String editProfile(Authentication auth, Model model) {

        String username = auth.getName();

        Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Student profile not linked with this user"));

        model.addAttribute("student", student);

        return "student-profile-edit";
    }

    // ✅ update profile
    @PostMapping("/edit")
    public String updateProfile(Authentication auth,
                                @RequestParam String name,
                                @RequestParam String roll) {

        String username = auth.getName();

        Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Student profile not linked with this user"));

        // only update allowed fields
        student.setName(name);
        student.setRoll(roll);

        studentRepository.save(student);

        return "redirect:/student/profile?updated";
    }
}
