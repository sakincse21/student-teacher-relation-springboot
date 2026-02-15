package com.example.webapp.repository;

import com.example.webapp.entity.Course;
import com.example.webapp.entity.Teacher;
import com.example.webapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveCourse_PersistsSuccessfully() {
        // Save dependent entities first
        User user = new User("testteacher", "encoded", com.example.webapp.entity.Role.TEACHER);
        user = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setFullName("Jane Smith");
        teacher.setEmail("jane@example.com");
        teacher.setUser(user);
        teacher = teacherRepository.save(teacher);

        Course course = new Course();
        course.setCourseCode("CSE-2102");
        course.setCourseTitle("Algorithms");
        course.setTeacher(teacher);

        Course savedCourse = courseRepository.save(course);

        assertNotNull(savedCourse.getId());
        assertEquals("CSE-2102", savedCourse.getCourseCode());
    }

    @Test
    void findByTeacherId_ReturnsCoursesForTeacher() {
        // Save dependent entities first
        User user = new User("findteacher", "encoded", com.example.webapp.entity.Role.TEACHER);
        user = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setFullName("Test Teacher");
        teacher.setEmail("test@example.com");
        teacher.setUser(user);
        teacher = teacherRepository.save(teacher);

        Course course1 = new Course("CSE-101", "Intro", teacher);
        Course course2 = new Course("CSE-102", "Advanced", teacher);
        courseRepository.save(course1);
        courseRepository.save(course2);

        List<Course> courses = courseRepository.findByTeacherId(teacher.getId());

        assertNotNull(courses);
        assertEquals(2, courses.size());
    }

    @Test
    void findAll_ReturnsAllCourses() {
        // Save dependent entities first
        User user = new User("allteacher", "encoded", com.example.webapp.entity.Role.TEACHER);
        user = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setFullName("Teacher");
        teacher.setEmail("teacher@test.com");
        teacher.setUser(user);
        teacher = teacherRepository.save(teacher);

        courseRepository.save(new Course("CSE-101", "Course 1", teacher));
        courseRepository.save(new Course("CSE-102", "Course 2", teacher));

        List<Course> courses = courseRepository.findAll();

        assertNotNull(courses);
        assertEquals(2, courses.size());
    }
}