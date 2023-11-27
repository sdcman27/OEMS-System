package edu.sru.thangiah.service;

import edu.sru.thangiah.domain.Course;
import edu.sru.thangiah.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing course-related operations such as saving individual courses or batches of courses.
 * Utilizes the {@link CourseRepository} for persistence operations.
 */
@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    
    /**
     * Persists a single course entity to the database.
     *
     * @param course The {@link Course} entity to be saved.
     */
    public void save(Course course) {
        courseRepository.save(course);
    }
    
    /**
     * Saves a collection of course entities to the database in a batch operation.
     *
     * @param courses The list of {@link Course} entities to be saved.
     */
    public void saveAll(List<Course> courses) {
        courseRepository.saveAll(courses);
    }
}
