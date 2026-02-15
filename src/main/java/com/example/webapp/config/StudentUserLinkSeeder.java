package com.example.webapp.config;

import com.example.webapp.entity.Student;
import com.example.webapp.entity.User;
import com.example.webapp.repository.StudentRepository;
import com.example.webapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")  // <-- ADD THIS LINE
public class StudentUserLinkSeeder implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public StudentUserLinkSeeder(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {

        User studentUser = userRepository.findByUsername("student").orElse(null);
        if (studentUser == null) return;

        // ✅ If already linked, do nothing
        if (studentRepository.findByUserUsername("student").isPresent()) {
            System.out.println("✅ 'student' user already linked to a student profile.");
            return;
        }

        // ✅ Find a student row that is not linked to any user
        Student freeStudent = studentRepository.findAll()
                .stream()
                .filter(s -> s.getUser() == null)
                .findFirst()
                .orElse(null);

        if (freeStudent == null) {
            System.out.println("⚠️ No free Student found to link with 'student' user.");
            return;
        }

        freeStudent.setUser(studentUser);
        studentRepository.save(freeStudent);

        System.out.println("✅ Linked 'student' login with Student id: " + freeStudent.getId());
    }
}
