package edu.sru.thangiah.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.sru.thangiah.domain.Course;
import edu.sru.thangiah.domain.Instructor;
import edu.sru.thangiah.repository.CourseRepository;
import edu.sru.thangiah.repository.InstructorRepository;



/**
 * Service class for handling operations related to schedule management.
 * <p>
 * Provides methods for creating or updating instructor and course information,
 * as well as for deleting instructors and courses from the system.
 * </p>
 *
 * Note: This class also seems to have an HTTP POST endpoint mapping,
 * which is typically not a responsibility of a service class. Service classes
 * should be free of web layer annotations like {@code @PostMapping}.
 * Such methods should be moved to a controller class.
 *
 * @author Your Name
 * @version 1.0
 */
@Service
public class ScheduleManagerService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    /**
     * Creates or updates an instructor in the database.
     *
     * @param instructor The instructor entity to save.
     * @return The saved instructor entity.
     */
    public Instructor createOrUpdateInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    /**
     * Creates or updates a course in the database.
     *
     * @param course The course entity to save.
     * @return The saved course entity.
     */
    public Course createOrUpdateCourse(Course course) {
        return courseRepository.save(course);
    }

    /**
     * Creates or updates a course in the database.
     *
     * @param course The course entity to save.
     * @return The saved course entity.
     */
    public void deleteInstructor(Long id) {
        instructorRepository.deleteById(id);
    }
    
    /**
     * Deletes an instructor by their identifier.
     *
     * @param id The unique identifier of the instructor to delete.
     */

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
    
    
    
 // Endpoint to associate an instructor with a course
    @PostMapping("/instructor/course/associate")
    public ResponseEntity<String> associateInstructorWithCourse(
        @RequestParam Long instructorId,
        @RequestParam Long courseId,
        Model model) {

        // Retrieve the instructor and course entities from the repository
        Instructor instructor = instructorRepository.findById(instructorId).orElse(null);
        Course course = courseRepository.findById(courseId).orElse(null);

        // Check if both entities exist
        if (instructor != null && course != null) {
            // Add the course to the instructor's courses
            instructor.getCourses().add(course);
            instructorRepository.save(instructor);
            return ResponseEntity.ok("Instructor associated with the course successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Instructor or course not found");
        }
    }

    
}
