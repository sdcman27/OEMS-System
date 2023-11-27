package edu.sru.thangiah.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import edu.sru.thangiah.domain.Course;
import edu.sru.thangiah.domain.Student;
import edu.sru.thangiah.model.User;

/**
* JPA repository for {@link Student} entities. This interface facilitates CRUD operations
* and custom queries for Student data within the database.
*/
public interface StudentRepository extends JpaRepository<Student, Long> {
	
	/**
     * Retrieves a Student entity by its username.
     *
     * @param username The username of the Student.
     * @return An {@link Optional} containing the Student if found, or empty otherwise.
     */
	Optional<Student> findByStudentUsername(String username);

	/**
     * Finds a list of Students where the student's first name contains the given string.
     *
     * @param name The string to search within the student's first name.
     * @return A list of {@link Student} entities that match the search criterion.
     */
	List<Student> findBystudentFirstNameContaining(String name);

	/**
     * Finds a list of Students where the student's username contains the given string.
     *
     * @param studentUsername The string to search within the student's username.
     * @return A list of {@link Student} entities that match the search criterion.
     */
	List<Student> findBystudentUsernameContaining(String studentUsername);
	
	/**
     * Retrieves a Student entity associated with a given User entity.
     *
     * @param user The {@link User} entity to search with.
     * @return An {@link Optional} containing the associated Student if found, or empty otherwise.
     */
	Optional<Student> findByUserId(User user);
	
	/**
     * Custom query to find a Student based on the user's ID.
     *
     * @param userId The identifier of the {@link User} associated with the Student.
     * @return An {@link Optional} containing the Student if found, or empty otherwise.
     */
    @Query("SELECT s FROM Student s WHERE s.user.id = :userId")
    Optional<Student> findByUserId(@Param("userId") Long userId);

    /**
     * Finds all Students that are enrolled in any of the given courses.
     *
     * @param courses The list of {@link Course} entities to search within.
     * @return A list of {@link Student} entities that are enrolled in the given courses.
     */
	List<Student> findAllByCoursesIn(List<Course> courses);

	List<Student> findByCoursesId(Long id);

	
	
}

