package com.example.webapp.controller;

import com.example.webapp.repository.TeacherRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
public class StudentTeacherController {

    private final TeacherRepository teacherRepository;

    public StudentTeacherController(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @GetMapping("/teachers")
    public String teacherList(Model model) {
        model.addAttribute("teachers", teacherRepository.findAll());
        return "student-teachers";
    }
}
