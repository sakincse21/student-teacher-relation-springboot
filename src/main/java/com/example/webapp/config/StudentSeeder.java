package com.example.webapp.config;

import com.example.webapp.entity.Role;
import com.example.webapp.entity.Student;
import com.example.webapp.entity.User;
import com.example.webapp.repository.StudentRepository;
import com.example.webapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")  // <-- ADD THIS LINE
public class StudentSeeder implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentSeeder(StudentRepository studentRepository,
                         UserRepository userRepository,
                         PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (studentRepository.count() > 0) {
            return; // avoid duplicate seeding
        }

        seedStudent("Student One", "2107010", "student1");
        seedStudent("Student Two", "2107011", "student2");
        seedStudent("Student Three", "2107012", "student3");

        System.out.println("✅ Seeded students with default password: student123");
    }

    private void seedStudent(String name, String roll, String username) {

        // create user
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("student123"));
        user.setRole(Role.STUDENT);
        userRepository.save(user);

        // create student
        Student student = new Student();
        student.setName(name);
        student.setRoll(roll);
        student.setUser(user);

        studentRepository.save(student);
    }
}
