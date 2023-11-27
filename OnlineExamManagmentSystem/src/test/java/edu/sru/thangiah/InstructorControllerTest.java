package edu.sru.thangiah;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import edu.sru.thangiah.controller.InstructorController;
import edu.sru.thangiah.domain.Instructor;
import edu.sru.thangiah.repository.InstructorRepository;

class InstructorControllerTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Autowired
    private InstructorController instructorController;

    @MockBean
    private InstructorRepository instructorRepository;

    @Test
    public void testCreateInstructor() {
        Instructor instructor = new Instructor();
        // Set instructor details
        instructor.setInstructorFirstName("John");
        instructor.setInstructorLastName("Doe");
        instructor.setInstructorUsername("johndoe");


        ResponseEntity<String> response = instructorController.createInstructor(instructor);
        //assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Instructor created successfully"));
    }

}
