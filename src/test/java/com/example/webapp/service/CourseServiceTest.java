package com.example.webapp.service;

import com.example.webapp.entity.Course;
import com.example.webapp.entity.Teacher;
import com.example.webapp.entity.User;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.repository.TeacherRepository;
import com.example.webapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void getCoursesByTeacherId_ReturnsCourses() {
        // Save dependent entities
        User user = new User("teacher1", "encoded", com.example.webapp.entity.Role.TEACHER);
        user = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setFullName("John Doe");
        teacher.setEmail("john@example.com");
        teacher.setUser(user);
        teacher = teacherRepository.save(teacher);

        Course c1 = new Course("CSE-2101", "Data Structures", teacher);
        Course c2 = new Course("CSE-2102", "Algorithms", teacher);
        courseRepository.save(c1);
        courseRepository.save(c2);

        List<Course> result = courseService.getCoursesByTeacherId(teacher.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getCoursesByTeacherId_ReturnsEmptyList() {
        List<Course> result = courseService.getCoursesByTeacherId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void saveCourse_SavesSuccessfully() {
        // Save dependent entities
        User user = new User("teacher2", "encoded", com.example.webapp.entity.Role.TEACHER);
        user = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setFullName("Jane Smith");
        teacher.setEmail("jane@example.com");
        teacher.setUser(user);
        teacher = teacherRepository.save(teacher);

        Course course = new Course();
        course.setCourseCode("CSE-301");
        course.setCourseTitle("Advanced Topics");
        course.setTeacher(teacher);

        Course result = courseService.save(course);

        assertNotNull(result);
        assertEquals("CSE-301", result.getCourseCode());
    }
}