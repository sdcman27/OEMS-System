package edu.sru.thangiah.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sru.thangiah.domain.Student;
import edu.sru.thangiah.repository.StudentRepository;


/**
 * Service class for handling business operations related to {@link Student} entities.
 * This service provides functionality to save a batch of student entities into the database.
 *
 * 
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    
    
    /**
     * Saves a set of student entities to the database.
     * This batch operation is useful for importing multiple students at once.
     *
     * @param students A set of {@link Student} entities to be persisted.
     */
    
    public void saveAll(Set<Student> students) {
        studentRepository.saveAll(students);
    }
}
