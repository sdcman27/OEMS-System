package edu.sru.thangiah;

import edu.sru.thangiah.domain.Course;
import edu.sru.thangiah.domain.Instructor;
import edu.sru.thangiah.model.Roles;
import edu.sru.thangiah.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class InstructorTest {

    private Instructor instructor;

    @BeforeEach
    void setUp() {
        instructor = new Instructor();
    }

    @Test
    void testInstructorId() {
        instructor.setInstructorId(1L);
        assertEquals(1L, instructor.getInstructorId());
    }

    @Test
    void testInstructorRoles() {
        List<Roles> roles = new ArrayList<>();
        roles.add(new Roles());
        instructor.setRoles(roles);
        assertEquals(roles, instructor.getRoles());
    }

    @Test
    void testInstructorFirstName() {
        instructor.setInstructorFirstName("John");
        assertEquals("John", instructor.getInstructorFirstName());
    }

    @Test
    void testInstructorLastName() {
        instructor.setInstructorLastName("Doe");
        assertEquals("Doe", instructor.getInstructorLastName());
    }

    @Test
    void testInstructorEmail() {
        instructor.setInstructorEmail("john.doe@example.com");
        assertEquals("john.doe@example.com", instructor.getInstructorEmail());
    }

    @Test
    void testInstructorPassword() {
        instructor.setInstructorPassword("password123");
        assertEquals("password123", instructor.getInstructorPassword());
    }

    @Test
    void testInstructorUsername() {
        instructor.setInstructorUsername("john_doe");
        assertEquals("john_doe", instructor.getInstructorUsername());
    }

    @Test
    void testCreditsTaught() {
        instructor.setCreditsTaught(10.0f);
        assertEquals(10.0f, instructor.getCreditsTaught());
    }

    @Test
    void testCourses() {
        Set<Course> courses = new HashSet<>();
        courses.add(new Course()); 
        instructor.setCourses(courses);
        assertEquals(courses, instructor.getCourses());
    }

    @Test
    void testSetUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setUsername("john_doe");
        instructor.setUser(user);
        assertEquals("John", instructor.getInstructorFirstName());
        assertEquals("Doe", instructor.getInstructorLastName());
        assertEquals("john.doe@example.com", instructor.getInstructorEmail());
        assertEquals("password123", instructor.getInstructorPassword());
        assertEquals("john_doe", instructor.getInstructorUsername());
    }
}
