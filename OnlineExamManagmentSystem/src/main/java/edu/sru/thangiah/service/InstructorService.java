package edu.sru.thangiah.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sru.thangiah.domain.Instructor;
import edu.sru.thangiah.exception.ResourceNotFoundException;
import edu.sru.thangiah.repository.InstructorRepository;

/**
 * Service class for managing instructors within the Online Exam Management System.
 * <p>
 * This class provides transactional operations for creating, updating, and deleting instructors.
 * It uses the {@link InstructorRepository} for database interactions.
 * </p>
 *
 *
 */
@Service
@Transactional
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    /**
     * Deletes an instructor by their identifier. If the instructor is not found, 
     * it throws a {@link ResourceNotFoundException}.
     *
     * @param instructorId The unique identifier of the instructor to delete.
     * @throws ResourceNotFoundException if the instructor is not found.
     */
    public void deleteInstructor(Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
            .orElseThrow(() -> new ResourceNotFoundException("Instructor not found: " + instructorId));
        
        instructorRepository.deleteById(instructorId);
    }
 
    /**
     * Saves a list of instructors to the database. This can be used for bulk insertion or update.
     *
     * @param instructors A list of {@link Instructor} entities to be persisted.
     */

    public void saveAll(List<Instructor> instructors) {
        instructorRepository.saveAll(instructors);
    }
    
    /**
     * Saves a single instructor entity to the database.
     *
     * @param instructor The {@link Instructor} entity to be persisted.
     */
    public void save(Instructor instructor) {
        instructorRepository.save(instructor);
    }
    
}
