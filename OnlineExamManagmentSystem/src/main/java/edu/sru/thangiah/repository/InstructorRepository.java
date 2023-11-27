package edu.sru.thangiah.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sru.thangiah.domain.Instructor;


/**
 * JPA repository for {@link Instructor} entities. It allows for standard CRUD operations and
 * provides methods for searching instructors by username and name substrings.
 */
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
	
	 /**
     * Retrieves an Instructor entity by its username.
     *
     * @param username The username to search for.
     * @return An {@link Optional} containing the matching Instructor if found, or empty if not.
     */
	Optional<Instructor> findByInstructorUsername(String username);

	/**
     * Finds a list of Instructor entities where the first name contains the given search parameter.
     *
     * @param searchParam The string to search for within the instructor's first name.
     * @return A list of {@link Instructor} entities with first names containing the search parameter.
     */
	List<Instructor> findByInstructorFirstNameContaining(String searchParam);

	/**
     * Finds a list of Instructor entities where the username contains the given search parameter.
     *
     * @param searchParam The string to search for within the instructor's username.
     * @return A list of {@link Instructor} entities with usernames containing the search parameter.
     */
	List<Instructor> findByInstructorUsernameContaining(String searchParam);
	
	

}

