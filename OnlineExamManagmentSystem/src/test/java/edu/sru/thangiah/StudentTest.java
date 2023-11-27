package edu.sru.thangiah;

import edu.sru.thangiah.domain.Course;
import edu.sru.thangiah.domain.Student;
import edu.sru.thangiah.model.Roles;
import edu.sru.thangiah.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
    }

    @Test
    void testStudentId() {
        student.setStudentId(1L);
        assertEquals(1L, student.getStudentId());
    }

    @Test
    void testStudentFirstName() {
        student.setStudentFirstName("Alice");
        assertEquals("Alice", student.getStudentFirstName());
    }

    @Test
    void testStudentLastName() {
        student.setStudentLastName("Smith");
        assertEquals("Smith", student.getStudentLastName());
    }

    @Test
    void testStudentEmail() {
        student.setStudentEmail("alice.smith@example.com");
        assertEquals("alice.smith@example.com", student.getStudentEmail());
    }

    @Test
    void testStudentPassword() {
        student.setStudentPassword("password123");
        assertEquals("password123", student.getStudentPassword());
    }

    @Test
    void testStudentUsername() {
        student.setStudentUsername("alice_smith");
        assertEquals("alice_smith", student.getStudentUsername());
    }

    @Test
    void testCreditsTaken() {
        student.setCreditsTaken(15.0f);
        assertEquals(15.0f, student.getCreditsTaken());
    }

    @Test
    void testCourses() {
        Set<Course> courses = new HashSet<>();
        courses.add(new Course());
        student.setCourses(courses);
        assertEquals(courses, student.getCourses());
    }

    @Test
    void testRoles() {
        List<Roles> roles = new ArrayList<>();
        roles.add(new Roles());
        student.setRoles(roles);
        assertEquals(roles, student.getRoles());
    }

    @Test
    void testEnabled() {
        student.setEnabled(true);
        assertTrue(student.isEnabled());
    }

    @Test
    void testUser() {
        User user = new User(); 
        student.setUser(user);
        assertEquals(user, student.getUser());
    }

    @Test
    void testToggleEnabled() {
        student.setEnabled(true);
        student.toggleEnabled();
        assertFalse(student.isEnabled());
    }
}
