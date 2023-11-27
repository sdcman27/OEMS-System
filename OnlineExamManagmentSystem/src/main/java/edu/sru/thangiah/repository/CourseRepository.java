package edu.sru.thangiah.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.sru.thangiah.domain.Course;
import edu.sru.thangiah.domain.Instructor;


/**
 * JPA repository for {@link Course} entities. This interface extends JpaRepository to provide
 * standard CRUD operations for Course data and includes additional querying capabilities.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {
	
	/**
     * Finds all Course entities containing a part of the ID in their identifiers.
     *
     * @param Id The partial ID to search for within the course identifiers.
     * @return A list of {@link Course} entities with IDs containing the given value.
     */
	List<Course> findByIdContaining(Long Id);
	
	 /**
     * Custom query to find all courses taught by a specific instructor.
     *
     * @param instructor The {@link Instructor} entity to search with.
     * @return A list of {@link Course} entities associated with the given instructor.
     */
	@Query("SELECT c FROM Course c WHERE c.instructor = :instructor")
    List<Course> findAllByInstructor(@Param("instructor") Instructor instructor);

}