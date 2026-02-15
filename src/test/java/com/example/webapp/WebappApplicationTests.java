package com.example.webapp;

import com.example.webapp.entity.*;
import com.example.webapp.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class WebappApplicationTests {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        assertNotNull(studentRepository);
        assertNotNull(teacherRepository);
        assertNotNull(courseRepository);
        assertNotNull(userRepository);
    }

    @Test
    void testStudentTeacherCourseRelationship() {
        // Create user for teacher
        User teacherUser = new User();
        teacherUser.setUsername("integration_teacher");
        teacherUser.setPassword(passwordEncoder.encode("password123"));
        teacherUser.setRole(Role.TEACHER);
        teacherUser = userRepository.save(teacherUser);

        // Create teacher
        Teacher teacher = new Teacher();
        teacher.setFullName("Integration Teacher");
        teacher.setEmail("integration@test.com");
        teacher.setUser(teacherUser);
        teacher = teacherRepository.save(teacher);

        // Create course
        Course course = new Course();
        course.setCourseCode("INT-101");
        course.setCourseTitle("Integration Test Course");
        course.setTeacher(teacher);
        course = courseRepository.save(course);

        // Create user for student
        User studentUser = new User();
        studentUser.setUsername("integration_student");
        studentUser.setPassword(passwordEncoder.encode("password123"));
        studentUser.setRole(Role.STUDENT);
        studentUser = userRepository.save(studentUser);

        // Create student
        Student student = new Student();
        student.setName("Integration Student");
        student.setRoll("2107999");
        student.setUser(studentUser);
        student = studentRepository.save(student);

        // Assign course to student
        student.getCourses().add(course);
        student = studentRepository.save(student);

        // Verify relationships
        Student savedStudent = studentRepository.findById(student.getId()).orElseThrow();
        assertNotNull(savedStudent);
        assertEquals(1, savedStudent.getCourses().size());
        assertEquals("INT-101", savedStudent.getCourses().iterator().next().getCourseCode());

        Teacher savedTeacher = teacherRepository.findById(teacher.getId()).orElseThrow();
        assertNotNull(savedTeacher);
        assertTrue(savedTeacher.getUser().getUsername().equals("integration_teacher"));
    }

    @Test
    void testCourseRepositoryQueries() {
        // Setup
        User teacherUser = new User("test_teacher", passwordEncoder.encode("pass"), Role.TEACHER);
        teacherUser = userRepository.save(teacherUser);

        Teacher teacher = new Teacher();
        teacher.setFullName("Test Teacher");
        teacher.setEmail("test@teacher.com");
        teacher.setUser(teacherUser);
        teacher = teacherRepository.save(teacher);

        // Create multiple courses
        Course course1 = new Course("CSE-101", "Course One", teacher);
        Course course2 = new Course("CSE-102", "Course Two", teacher);
        courseRepository.save(course1);
        courseRepository.save(course2);

        // Test queries
        List<Course> allCourses = courseRepository.findAll();
        assertEquals(2, allCourses.size());

        List<Course> teacherCourses = courseRepository.findByTeacherId(teacher.getId());
        assertEquals(2, teacherCourses.size());
    }

    @Test
    void testStudentRepositoryOperations() {
        // Create and save student
        Student student = new Student();
        student.setName("Test Integration");
        student.setRoll("2107888");
        Student saved = studentRepository.save(student);

        // Retrieve and verify
        Student retrieved = studentRepository.findById(saved.getId()).orElseThrow();
        assertEquals("Test Integration", retrieved.getName());
        assertEquals("2107888", retrieved.getRoll());

        // Update
        retrieved.setName("Updated Name");
        studentRepository.save(retrieved);
        Student updated = studentRepository.findById(saved.getId()).orElseThrow();
        assertEquals("Updated Name", updated.getName());

        // Delete
        studentRepository.delete(updated);
        assertFalse(studentRepository.findById(updated.getId()).isPresent());
    }
}