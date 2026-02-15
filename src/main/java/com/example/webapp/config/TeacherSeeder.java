package com.example.webapp.config;

import com.example.webapp.entity.Teacher;
import com.example.webapp.entity.User;
import com.example.webapp.repository.TeacherRepository;
import com.example.webapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")  // <-- ADD THIS LINE
public class TeacherSeeder implements CommandLineRunner {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    public TeacherSeeder(TeacherRepository teacherRepository, UserRepository userRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        User teacherUser = userRepository.findByUsername("teacher").orElse(null);
        if (teacherUser == null) return;

        if (teacherRepository.findByUserUsername("teacher").isPresent()) return;

        Teacher t = new Teacher();
        t.setFullName("Demo Teacher");
        t.setEmail("teacher@demo.com");
        t.setUser(teacherUser);

        teacherRepository.save(t);

        System.out.println("✅ Demo Teacher profile created.");
    }
}
