package com.example.webapp.config;

import com.example.webapp.entity.Student;
import com.example.webapp.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")  // <-- ADD THIS LINE
public class DataSeeder implements CommandLineRunner {

    private final StudentRepository studentRepository;

    public DataSeeder(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) {

        // Don't insert every time (avoid duplicate data)
        if (studentRepository.count() > 0) {
            return;
        }

        Student s1 = new Student();
        s1.setName("Sakin");
        s1.setRoll("2107010");

        Student s2 = new Student();
        s2.setName("Faiyaz");
        s2.setRoll("2107011");

        Student s3 = new Student();
        s3.setName("Rafi");
        s3.setRoll("2107012");

        studentRepository.save(s1);
        studentRepository.save(s2);
        studentRepository.save(s3);

        System.out.println("✅ Sample students inserted into DB!");
    }
}
