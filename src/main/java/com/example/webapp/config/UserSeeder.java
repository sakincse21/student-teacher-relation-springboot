package com.example.webapp.config;

import com.example.webapp.entity.Role;
import com.example.webapp.entity.User;
import com.example.webapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")  // <-- ADD THIS LINE
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (userRepository.findByUsername("teacher").isEmpty()) {
            userRepository.save(new User(
                    "teacher",
                    passwordEncoder.encode("teacher123"),
                    Role.TEACHER
            ));
        }

        if (userRepository.findByUsername("student").isEmpty()) {
            userRepository.save(new User(
                    "student",
                    passwordEncoder.encode("student123"),
                    Role.STUDENT
            ));
        }

        System.out.println("✅ Demo users ready: teacher/teacher123 and student/student123");
    }
}
