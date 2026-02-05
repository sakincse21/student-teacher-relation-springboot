package com.example.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teacher")
public class TeacherDashboardController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "teacher-dashboard";
    }
}
